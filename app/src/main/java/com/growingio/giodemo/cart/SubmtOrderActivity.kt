package com.growingio.giodemo.cart

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.growingio.giodemo.*
import kotlinx.android.synthetic.main.activity_submt_order.*
import kotlinx.android.synthetic.main.include_page_title.*

class SubmtOrderActivity : AppCompatActivity() {
    private var productObj: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submt_order)

        back.setOnClickListener { finish() }
        head_title.text = "填写订单"


        var orderFee = 0
        var payPrice = 0
        for (product in listProduct!!) {
            orderFee += product.price
        }

        payPrice = orderFee + 10

        order_fee.text = String.format("￥ $orderFee")
        tv_pay_price.text = String.format("￥ $payPrice")

        rc_submit_order.adapter = MyGoodsAdapter(this, listProduct!!, false)
        rc_submit_order.layoutManager = LinearLayoutManager(this)

        submit_order.setOnClickListener {
            startActivity(
                Intent(this, CounterActivity::class.java).putExtra(
                    "price",
                    payPrice
                )
            )
        }

    }

}
