package com.growingio.androiddemo.base

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.growingio.androiddemo.R
import com.journeyapps.barcodescanner.CaptureManager
import kotlinx.android.synthetic.main.activity_zxing_layout.*

/**
 * @Class: CustomCaptureActivity
 * @Description: 自定义条形码/二维码扫描
 * @Author: wangnan7
 * @Date: 2017/5/19
 */

class CustomCaptureActivity : AppCompatActivity() {

    /**
     * 条形码扫描管理器
     */
    private var mCaptureManager: CaptureManager? = null

    /**
     * 条形码扫描视图
     */

    /**
     * 标题栏
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindow()
        setContentView(R.layout.activity_zxing_layout)
        initToolbar()
        mCaptureManager = CaptureManager(this, zxing_barcode_scanner)
        mCaptureManager!!.initializeFromIntent(intent, savedInstanceState)
        mCaptureManager!!.decode()
    }


    /**
     * 初始化窗口
     */
    private fun initWindow() {
        // API_19及其以上透明掉状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val layoutParams = window.attributes
            layoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or layoutParams.flags
        }
    }

    /**
     * 初始化标题栏
     */
    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "二维码扫描"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        mCaptureManager!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mCaptureManager!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCaptureManager!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mCaptureManager!!.onSaveInstanceState(outState)
    }

    /**
     * 权限处理
     */
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        mCaptureManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * 按键处理
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return zxing_barcode_scanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

}
