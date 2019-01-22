package com.growingio.giodemo.cart

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.growingio.giodemo.R

class ShoppingCartFragment : Fragment() {


    companion object {
        fun newInstance(): ShoppingCartFragment {
            return ShoppingCartFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_cart, container, false)

        val sbmtOrder = view.findViewById<View>(R.id.sbmt_order) as Button

        sbmtOrder.setOnClickListener {
            startActivity(Intent(activity, SubmtOrderActivity::class.java))
        }
        return view
    }
}
