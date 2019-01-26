package com.growingio.giodemo.cart

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.growingio.giodemo.Product
import com.growingio.giodemo.R
import kotlinx.android.synthetic.main.activity_submt_order.*
import kotlinx.android.synthetic.main.include_page_title.*

class SubmtOrderActivity : AppCompatActivity() {
    private var productObj: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submt_order)

        back.setOnClickListener { finish() }
        head_title.text = "填写订单"

        productObj = com.growingio.giodemo.syncProduct(intent)
        img_product_order.setImageResource(productObj!!.categoryImg)
        tv_product_name.text = productObj!!.name
        tv_product_price.text = String.format("￥ ${productObj!!.price}")
        order_fee.text = String.format("￥ ${productObj!!.price}")
        tv_pay_price.text = String.format("￥ ${productObj!!.price + 10}")

        submit_order.setOnClickListener { startActivity(Intent(this, CounterActivity::class.java)) }

    }

}
