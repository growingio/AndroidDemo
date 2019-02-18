package com.growingio.giodemo.cart

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.growingio.android.sdk.collection.GrowingIO
import com.growingio.giodemo.*
import kotlinx.android.synthetic.main.activity_counter.*
import kotlinx.android.synthetic.main.include_page_title.*
import org.json.JSONObject

class CounterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        head_title.text = "收银台"
        back.setOnClickListener { finish() }

        val bill = intent.getIntExtra("price", 0)
        val num = intent.getIntExtra("num", 0)
        val orderId = intent.getStringExtra("orderid")

        tv_bill.text = String.format("￥ $bill")
        tv_pay_bill.text = String.format("￥ $bill")

        submit_order.setOnClickListener {
            GrowingIO.getInstance().track(
                "payOrder",
                JSONObject()
                    .put(GioOrderId, orderId)
                    .put(GioBuyQuantity, num)
                    .put(GioOrderAmount, bill)
                    .put(GioPayMethod, "GIO支付")
            )

            for (product in listProduct!!) {
                GrowingIO.getInstance().track(
                    "paySPU",
                    JSONObject()
                        .put(GioOrderId, orderId)
                        .put(GioBuyQuantity, 1)
                        .put(GioOrderAmount, product.price)
                        .put(GioProductName, product.name)
                        .put(GioProductId, product.id)
                        .put(GioPayMethod, "GIO支付")
                )
            }

            startActivity(
                Intent(this, PaySuccessActivity::class.java)
                    .putExtra(GioPayment, bill)
                    .putExtra("orderid", orderId)
            )
        }

    }
}
