package com.growingio.giodemo.cart

import android.content.Context
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
    private lateinit var adapter: MyGoodsAdapter
    private var mIsAttach = false
    private var mIsVisible = false
    private var mIsCreated = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(userVisibleHint){
            mIsVisible = true
            lazyLoad()
        }else{
            mIsVisible = false
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (!mIsAttach) {
            mIsAttach = true
            lazyLoad()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_cart, container, false)

        adapter = MyGoodsAdapter(activity!!, listProduct!!, null, null)
        view.rc_cart.adapter = adapter

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
                        .put(GioPayment, product.price)
                )
            }

        }

        if (!mIsCreated) {
            mIsCreated = true
            lazyLoad()
        }
        return view
    }

    override fun onDetach() {
        super.onDetach()
        mIsAttach = false
    }

    private fun lazyLoad() {
        if (mIsVisible && mIsAttach && mIsCreated) {
            productInTheCart(context!!)
            adapter.notifyDataSetChanged()
        }
    }
}