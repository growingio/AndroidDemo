package com.growingio.giodemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.growingio.giodemo.cart.SubmtOrderActivity
import com.growingio.giodemo.cart.defaultPrefs
import com.growingio.giodemo.cart.set
import kotlinx.android.synthetic.main.activity_product_detail.*

class ProductDetailActivity : AppCompatActivity(), View.OnClickListener {
    private var shareDialog: AlertDialog? = null
    private var shareSuccessDialog: AlertDialog? = null
    private var shareDesc: TextView? = null
    private var productObj: Product? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        img_back.setOnClickListener { finish() }

        comments.setOnClickListener(this)
        share.setOnClickListener(this)
        btn_add_to_cart.setOnClickListener(this)
        btn_cash_now.setOnClickListener(this)

        shareDialog = AlertDialog.Builder(this).create()
        shareSuccessDialog = AlertDialog.Builder(this).create()

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_share, null)
        val successViewView = LayoutInflater.from(this).inflate(R.layout.dialog_share_success, null)

        shareDialog!!.setCanceledOnTouchOutside(true)
        shareSuccessDialog!!.setCanceledOnTouchOutside(true)

        dialogView.findViewById<View>(R.id.moments).setOnClickListener(this)
        dialogView.findViewById<View>(R.id.wechat).setOnClickListener(this)
        dialogView.findViewById<View>(R.id.weibo).setOnClickListener(this)
        dialogView.findViewById<View>(R.id.qq).setOnClickListener(this)

        successViewView.findViewById<View>(R.id.btn_ok).setOnClickListener(this)
        shareDesc = successViewView.findViewById<View>(R.id.tv_share_desc) as TextView

        shareDialog!!.setView(dialogView)
        shareSuccessDialog!!.setView(successViewView)


        productObj = syncProduct(intent)
        img_product.setImageResource(productObj!!.sellImg)
        product_name.text = productObj!!.name
        tv_desc.text = productObj!!.desc
        tv_price.text = String.format("￥ ${productObj!!.price}")

    }


    override fun onClick(v: View?) {
        val formatString = "【%s】已经分享到【%s】"
        when (v!!.id) {
            R.id.comments -> startActivity(Intent(this, CommentsActivity::class.java))

            R.id.share -> {
                shareDialog!!.show()
            }
            R.id.weibo -> {
                shareDesc!!.text = String.format(formatString, productObj!!.name, "微博")
                shareSuccess()
            }
            R.id.wechat -> {
                shareDesc!!.text = String.format(formatString, productObj!!.name, "微信")
                shareSuccess()
            }
            R.id.moments -> {
                shareDesc!!.text = String.format(formatString, productObj!!.name, "朋友圈")
                shareSuccess()
            }
            R.id.qq -> {
                shareDesc!!.text = String.format(formatString, productObj!!.name, "QQ")
                shareSuccess()
            }

            R.id.btn_ok -> {
                shareSuccessDialog!!.dismiss()
            }
            R.id.btn_add_to_cart -> {
                val preferences = defaultPrefs(this)
                preferences[productObj!!.name] = true
                Toast.makeText(this, "加入购物车成功", Toast.LENGTH_SHORT).show()
            }
            R.id.btn_cash_now -> {
                startActivity(Intent(this, SubmtOrderActivity::class.java).putExtra("product", productObj!!.name))
            }
        }

    }

    private fun shareSuccess() {
        if (shareDialog!!.isShowing) {
            shareDialog!!.dismiss()
        }
        shareSuccessDialog!!.show()
    }
}
