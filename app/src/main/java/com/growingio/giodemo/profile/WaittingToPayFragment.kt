package com.growingio.giodemo.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.growingio.giodemo.R
import com.growingio.giodemo.gioCup

class WaittingToPayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_order, container, false)
        val num = view.findViewById<View>(R.id.tv_pd_num) as TextView
        val name = view.findViewById<View>(R.id.tv_pd_name) as TextView
        val price = view.findViewById<View>(R.id.tv_pd_price) as TextView
        num.text = String.format("订单编号：${gioCup.orderNumber}")
        name.text = gioCup.name
        price.text = String.format("￥ ${gioCup.price}")

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() = WaittingToPayFragment()
    }
}
