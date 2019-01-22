package com.growingio.giodemo.cart

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.growingio.giodemo.R
import kotlinx.android.synthetic.main.include_page_title.*

class CounterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        head_title.text = "收银台"
        back.setOnClickListener { finish() }
    }
}
