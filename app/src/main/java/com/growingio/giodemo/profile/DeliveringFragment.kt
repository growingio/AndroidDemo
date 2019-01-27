package com.growingio.giodemo.profile

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.growingio.giodemo.R
import com.growingio.giodemo.gioShirt

class DeliveringFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delivering, container, false)
        val num = view.findViewById<View>(R.id.tv_pd_num) as TextView
        val name = view.findViewById<View>(R.id.tv_pd_name) as TextView
        val price = view.findViewById<View>(R.id.tv_pd_price) as TextView
        val img = view.findViewById<View>(R.id.img_product_icon) as ImageView
        val btnStatus = view.findViewById<View>(R.id.btn_status) as Button
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnStatus.background = activity!!.getDrawable(R.drawable.bg_yellow)
            btnStatus.text = "配送中"
        }
        img.setImageResource(gioShirt.categoryImg)
        name.text = gioShirt.name
        price.text = String.format("￥ ${gioShirt.price}")
        num.text = String.format("订单编号：${gioShirt.orderNumber}")

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = DeliveringFragment()
    }
}
