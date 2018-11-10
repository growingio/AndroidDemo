package com.growingio.androiddemo.base

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Rect
import android.graphics.Shader
import android.util.AttributeSet
import android.util.TypedValue

import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.ViewfinderView

import java.util.ArrayList

/**
 * @Class: CustomViewfinderView
 * @Description: 自定义扫描框样式
 * @Author: wangnan7
 * @Date: 2017/5/22
 */

class CustomViewfinderView(context: Context, attrs: AttributeSet) : ViewfinderView(context, attrs) {

    /* ******************************************    边角线相关属性    ************************************************/

    /**
     * "边角线长度/扫描边框长度"的占比 (比例越大，线越长)
     */
    var mLineRate = 0.1f

    /**
     * 边角线厚度 (建议使用dp)
     */
    var mLineDepth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics)

    /**
     * 边角线颜色
     */
    var mLineColor = Color.WHITE

    /* *******************************************    扫描线相关属性    ************************************************/

    /**
     * 扫描线起始位置
     */
    var mScanLinePosition = 0

    /**
     * 扫描线厚度
     */
    var mScanLineDepth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics)

    /**
     * 扫描线每次重绘的移动距离
     */
    var mScanLineDy = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)

    /**
     * 线性梯度
     */
    lateinit var mLinearGradient: LinearGradient

    /**
     * 线性梯度位置
     */
    var mPositions = floatArrayOf(0f, 0.5f, 1f)

    /**
     * 线性梯度各个位置对应的颜色值
     */
    var mScanLineColor = intArrayOf(0x00FFFFFF, Color.WHITE, 0x00FFFFFF)

    override fun onDraw(canvas: Canvas) {
        refreshSizes()
        if (framingRect == null || previewFramingRect == null) {
            return
        }

        val frame = framingRect
        val previewFrame = previewFramingRect

        val width = canvas.width
        val height = canvas.height

        //绘制4个角
        paint.color = mLineColor // 定义画笔的颜色
        canvas.drawRect(frame.left.toFloat(), frame.top.toFloat(), frame.left + frame.width() * mLineRate, frame.top + mLineDepth, paint)
        canvas.drawRect(frame.left.toFloat(), frame.top.toFloat(), frame.left + mLineDepth, frame.top + frame.height() * mLineRate, paint)

        canvas.drawRect(frame.right - frame.width() * mLineRate, frame.top.toFloat(), frame.right.toFloat(), frame.top + mLineDepth, paint)
        canvas.drawRect(frame.right - mLineDepth, frame.top.toFloat(), frame.right.toFloat(), frame.top + frame.height() * mLineRate, paint)

        canvas.drawRect(frame.left.toFloat(), frame.bottom - mLineDepth, frame.left + frame.width() * mLineRate, frame.bottom.toFloat(), paint)
        canvas.drawRect(frame.left.toFloat(), frame.bottom - frame.height() * mLineRate, frame.left + mLineDepth, frame.bottom.toFloat(), paint)

        canvas.drawRect(frame.right - frame.width() * mLineRate, frame.bottom - mLineDepth, frame.right.toFloat(), frame.bottom.toFloat(), paint)
        canvas.drawRect(frame.right - mLineDepth, frame.bottom - frame.height() * mLineRate, frame.right.toFloat(), frame.bottom.toFloat(), paint)

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.color = if (resultBitmap != null) resultColor else maskColor
        canvas.drawRect(0f, 0f, width.toFloat(), frame.top.toFloat(), paint)
        canvas.drawRect(0f, frame.top.toFloat(), frame.left.toFloat(), (frame.bottom + 1).toFloat(), paint)
        canvas.drawRect((frame.right + 1).toFloat(), frame.top.toFloat(), width.toFloat(), (frame.bottom + 1).toFloat(), paint)
        canvas.drawRect(0f, (frame.bottom + 1).toFloat(), width.toFloat(), height.toFloat(), paint)

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.alpha = ViewfinderView.CURRENT_POINT_OPACITY
            canvas.drawBitmap(resultBitmap, null, frame, paint)
        } else {
            // 绘制扫描线
            mScanLinePosition += mScanLineDy.toInt()
            if (mScanLinePosition > frame.height()) {
                mScanLinePosition = 0
            }
            mLinearGradient = LinearGradient(frame.left.toFloat(), (frame.top + mScanLinePosition).toFloat(), frame.right.toFloat(), (frame.top + mScanLinePosition).toFloat(), mScanLineColor, mPositions, Shader.TileMode.CLAMP)
            paint.shader = mLinearGradient
            canvas.drawRect(frame.left.toFloat(), (frame.top + mScanLinePosition).toFloat(), frame.right.toFloat(), frame.top.toFloat() + mScanLinePosition.toFloat() + mScanLineDepth, paint)
            paint.shader = null

            val scaleX = frame.width() / previewFrame.width().toFloat()
            val scaleY = frame.height() / previewFrame.height().toFloat()

            val currentPossible = possibleResultPoints
            val currentLast = lastPossibleResultPoints
            val frameLeft = frame.left
            val frameTop = frame.top
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null
            } else {
                possibleResultPoints = ArrayList(5)
                lastPossibleResultPoints = currentPossible
                paint.alpha = ViewfinderView.CURRENT_POINT_OPACITY
                paint.color = resultPointColor
                for (point in currentPossible) {
                    canvas.drawCircle((frameLeft + (point.x * scaleX).toInt()).toFloat(),
                            (frameTop + (point.y * scaleY).toInt()).toFloat(),
                            ViewfinderView.POINT_SIZE.toFloat(), paint)
                }
            }
            if (currentLast != null) {
                paint.alpha = ViewfinderView.CURRENT_POINT_OPACITY / 2
                paint.color = resultPointColor
                val radius = ViewfinderView.POINT_SIZE / 2.0f
                for (point in currentLast) {
                    canvas.drawCircle((frameLeft + (point.x * scaleX).toInt()).toFloat(),
                            (frameTop + (point.y * scaleY).toInt()).toFloat(),
                            radius, paint)
                }
            }
        }

        // Request another update at the animation interval, but only repaint the laser line,
        // not the entire viewfinder mask.
        postInvalidateDelayed(CUSTOME_ANIMATION_DELAY,
                frame.left,
                frame.top,
                frame.right,
                frame.bottom)
    }

    companion object {

        /**
         * 重绘时间间隔
         */
        val CUSTOME_ANIMATION_DELAY: Long = 16
    }
}
