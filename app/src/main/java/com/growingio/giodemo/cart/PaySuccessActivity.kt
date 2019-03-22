package com.growingio.giodemo.cart

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.growingio.android.sdk.collection.GrowingIO
import com.growingio.giodemo.*
import com.growingio.giodemo.profile.MyOrderActivity
import kotlinx.android.synthetic.main.activity_pay_success.*
import org.json.JSONObject

class PaySuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_success)

        val id = intent.getStringExtra("orderid")
        val pay = intent.getIntExtra(GioPayment, 0)

        tv_order_desc.text = "订单编号：$id \n\n支付方式：GIO 支付\n\n支付金额：$pay\n\n优惠金额：￥ 0"

        //订单支付成功次数。用户支付成功时触发，一个订单发送一次事件。
        GrowingIO.getInstance().track(
            "payOrderSuccess",
            JSONObject()
                .put(GioOrderId, id)
                .put(GioPayMethod, "GIO 支付")
                .put(GioPayment, pay)
                .put(GioBuyQuantity, listProduct!!.size)
        )

        val preference = defaultPrefs(this)


        var evar: String?
        var evarSearch: String?

        for (product in listProduct!!) {
            //商品支付成功次数。用户支付成功时触发，一个订单中每种商品发送一次事件。例如一个订单中购买了2个A，3个B，4个C，发送3次Pay SPU Success事件
            GrowingIO.getInstance().track(
                "paySPUSuccess", JSONObject()
                    .put(GioProductId, product.id)
                    .put(GioProductName, product.name)
                    .put(GioBuyQuantity, listProduct!!.size)
                    .put(GioPayment, pay)
                    .put(GioOrderId, id)
                    .put(GioPayMethod, "GIO 支付")
            )

            evar = preference.getString(product.name + "_evar", null)
            evarSearch = preference.getString(product.name + "search", null)

            if (evar != null) {
                //用于衡量不同流量位带来的业务贡献。
                //取值包括：首页所有流量位，包括Banner、限时秒杀、GIO推荐、商品详情页推荐位
                GrowingIO.getInstance().setEvar("floor_evar", evar)
            }

            if (evarSearch != null) {
                //用于衡量不同搜索词带来的业务贡献。
                GrowingIO.getInstance().setEvar("searchWord_evar", evarSearch)
            }


        }


        tv_finish.setOnClickListener { finish() }
        btn_to_order.setOnClickListener { startActivity(Intent(this, MyOrderActivity::class.java)) }

    }
}
