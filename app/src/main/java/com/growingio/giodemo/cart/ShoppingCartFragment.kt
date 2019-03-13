package com.growingio.giodemo.cart

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growingio.android.sdk.collection.GrowingIO
import com.growingio.giodemo.*
import kotlinx.android.synthetic.main.fragment_shopping_cart.view.*
import org.json.JSONObject

class ShoppingCartFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_cart, container, false)

        productInTheCart(activity!!)
        view.rc_cart.adapter = MyGoodsAdapter(activity!!, listProduct!!, null, null)
        view.rc_cart.layoutManager = LinearLayoutManager(activity)

        view.btn_sbmt_order.isEnabled = listProduct!!.size > 0

        view.btn_sbmt_order.setOnClickListener {
            startActivity(Intent(activity, SubmtOrderActivity::class.java))

            for (product in listProduct!!) {
                GrowingIO.getInstance().track(
                    "checkOut",
                    JSONObject()
                        .put(GioProductId, product.id)
                        .put(GioProductName, product.name)
                        .put(GioBuyQuantity, 1)
                        .put(GioOrderAmount, product.price)
                )
            }

        }
        return view
    }


}


