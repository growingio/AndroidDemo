package com.growingio.giodemo.cart

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.growingio.giodemo.*

class ShoppingCartFragment : Fragment() {


    companion object {
        fun newInstance(): ShoppingCartFragment {
            return ShoppingCartFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_cart, container, false)

        val sbmtOrder = view.findViewById<View>(R.id.sbmt_order) as Button

        var listProduct: MutableList<Product>? = ArrayList()

        if (defaultPrefs(activity!!)[gioCup.name]!!) {
            listProduct!!.add(gioCup)
        }
        if (defaultPrefs(activity!!)[gioShirt.name]!!) {
            listProduct!!.add(gioShirt)
        }
        if (defaultPrefs(activity!!)[theHandBookOfDataOperation.name]!!) {
            listProduct!!.add(theHandBookOfDataOperation)
        }
        if (defaultPrefs(activity!!)[theHandBookOfGrowthHacker.name]!!) {
            listProduct!!.add(theHandBookOfGrowthHacker)
        }
        if (defaultPrefs(activity!!)[theHandBookOfPMAnalytics.name]!!) {
            listProduct!!.add(theHandBookOfPMAnalytics)
        }

        var rcCart = view.findViewById<View>(R.id.rc_cart) as RecyclerView
        rcCart.adapter = MyGoodsAdapter(activity!!, listProduct!!, true)
        rcCart.layoutManager = LinearLayoutManager(activity)

        sbmtOrder.setOnClickListener {
            startActivity(Intent(activity, SubmtOrderActivity::class.java))
        }
        return view
    }
}
