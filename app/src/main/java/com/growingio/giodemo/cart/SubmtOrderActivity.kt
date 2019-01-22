package com.growingio.giodemo.cart

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.growingio.giodemo.R
import kotlinx.android.synthetic.main.activity_submt_order.*
import kotlinx.android.synthetic.main.include_page_title.*

class SubmtOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submt_order)

        back.setOnClickListener { finish() }
        head_title.text = "填写订单"

        submit_order.setOnClickListener { startActivity(Intent(this, CounterActivity::class.java)) }

    }
}
