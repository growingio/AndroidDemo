package com.growingio.giodemo

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.growingio.android.sdk.collection.GrowingIO
import com.growingio.giodemo.cart.SubmtOrderActivity
import com.growingio.giodemo.cart.defaultPrefs
import com.growingio.giodemo.cart.set
import com.growingio.giodemo.category.CommentsActivity
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.dialog_share.view.*
import kotlinx.android.synthetic.main.dialog_share_success.view.*
import org.json.JSONObject

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
        suggest1.setOnClickListener(this)
        suggest2.setOnClickListener(this)

        shareDialog = AlertDialog.Builder(this).create()
        shareSuccessDialog = AlertDialog.Builder(this).create()

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_share, null)
        val successViewView = LayoutInflater.from(this).inflate(R.layout.dialog_share_success, null)

        shareDialog!!.setCanceledOnTouchOutside(true)
        shareSuccessDialog!!.setCanceledOnTouchOutside(true)

        dialogView.moments.setOnClickListener(this)
        dialogView.wechat.setOnClickListener(this)
        dialogView.weibo.setOnClickListener(this)
        dialogView.qq.setOnClickListener(this)

        successViewView.btn_ok.setOnClickListener(this)
        shareDesc = successViewView.tv_share_desc

        shareDialog!!.setView(dialogView)
        shareSuccessDialog!!.setView(successViewView)


        productObj = syncProduct(intent)

        GrowingIO.getInstance().track(
            "goodsDetailPageView",
            JSONObject()
                .put(GioProductId, productObj!!.id)
                .put(GioProductName, productObj!!.name)
        )

        img_product.setImageResource(productObj!!.sellImg)
        product_name.text = productObj!!.name
        tv_desc.text = productObj!!.desc
        tv_price.text = String.format("￥ ${productObj!!.price}")

        nested_sv.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
            trackAdPosition(img_book, "商品详情页推荐位", theHandBookOfGrowthHacker)
            trackAdPosition(img_shirts, "商品详情页推荐位", gioShirt)
        })
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
                shareSuccess("微博")
            }
            R.id.wechat -> {
                shareDesc!!.text = String.format(formatString, productObj!!.name, "微信")
                shareSuccess("微信")
            }
            R.id.moments -> {
                shareDesc!!.text = String.format(formatString, productObj!!.name, "朋友圈")
                shareSuccess("朋友圈")
            }
            R.id.qq -> {
                shareDesc!!.text = String.format(formatString, productObj!!.name, "QQ")
                shareSuccess("QQ")
            }

            R.id.btn_ok -> {
                shareSuccessDialog!!.dismiss()
            }
            R.id.btn_add_to_cart -> {
                GrowingIO.getInstance().track(
                    "addToCart",
                    JSONObject()
                        .put(GioProductId, productObj!!.id)
                        .put(GioProductName, productObj!!.name)
                        .put(GioBuyQuantity, 1)
                )

                defaultPrefs(this)[productObj!!.name] = true
                //用于衡量不同流量位带来的业务贡献。
                //取值包括：首页所有流量位，包括Banner、限时秒杀、GIO推荐、商品详情页推荐位
                defaultPrefs(this)[productObj!!.name + "_evar"] = intent.getStringExtra("evar")

                Toast.makeText(this, "加入购物车成功", Toast.LENGTH_SHORT).show()

            }
            R.id.btn_cash_now -> {

                defaultPrefs(this)[productObj!!.name] = true
                //用于衡量不同流量位带来的业务贡献。
                //取值包括：首页所有流量位，包括Banner、限时秒杀、GIO推荐、商品详情页推荐位
                defaultPrefs(this)[productObj!!.name + "_evar"] = intent.getStringExtra("evar")

                startActivity(
                    Intent(this, SubmtOrderActivity::class.java)
                        .putExtra("product", productObj!!.name)
                )
            }

            R.id.suggest1 -> {
                startActivity(
                    Intent(this, ProductDetailActivity::class.java)
                        .putExtra("product", theHandBookOfGrowthHacker.name)
                        .putExtra("evar", EVAR_PRODUCTS_SUGGESTED)
                )

            }
            R.id.suggest2 -> {
                startActivity(
                    Intent(this, ProductDetailActivity::class.java)
                        .putExtra("product", gioShirt.name)
                        .putExtra("evar", EVAR_PRODUCTS_SUGGESTED)
                )
            }
        }

    }

    private fun shareSuccess(channel: String) {
        if (shareDialog!!.isShowing) {
            shareDialog!!.dismiss()
        }
        shareSuccessDialog!!.show()

        //分享成功时触发,分享渠道
        GrowingIO.getInstance().track("shareActivity", JSONObject().put(GioShareChannel, channel))
    }
}
