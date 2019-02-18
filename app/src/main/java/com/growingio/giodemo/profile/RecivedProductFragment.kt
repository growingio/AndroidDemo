package com.growingio.giodemo.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.growingio.giodemo.R

class RecivedProductFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recived_product, container, false)
        val statusBtn = view.findViewById<View>(R.id.btn_status) as TextView
        statusBtn.text = "已收货"
        statusBtn.isEnabled = false
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = RecivedProductFragment()
    }
}
