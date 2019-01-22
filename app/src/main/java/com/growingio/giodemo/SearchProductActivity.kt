package com.growingio.giodemo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_search_product.*


class SearchProductActivity : AppCompatActivity(), View.OnClickListener {

    private var mInputMethodManager: InputMethodManager? = null
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product)

        mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        back.setOnClickListener(this)
        clear.setOnClickListener(this)
        search.setOnClickListener(this)
        et_search.setOnClickListener(this)
        et_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Toast.makeText(this, "搜索 " + et_search.text, Toast.LENGTH_SHORT).show()

                //----GrowingIO SDK User Attention ! 为了采集 chng 事件（输入框输入内容事件)， 需要使得输入框失去焦点，才会采集
                et_search.setFocusable(false)

                true
            }
            false
        }

    }


    @SuppressLint("NewApi")
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back -> finish()
            R.id.et_search -> {
                et_search.isFocusableInTouchMode = true
                et_search.setFocusable(true)
            }
            R.id.search -> {
                Toast.makeText(this, "搜索 " + et_search.text, Toast.LENGTH_SHORT).show()

                //----GrowingIO SDK User Attention ! 为了采集 chng 事件（输入框输入内容事件)， 需要使得输入框失去焦点，才会采集
                et_search.setFocusable(false)
                if (mInputMethodManager!!.isActive) {
                    mInputMethodManager!!.hideSoftInputFromWindow(et_search.windowToken, 0)
                }
            }
            R.id.clear -> et_search.setText("")
        }

    }
}
