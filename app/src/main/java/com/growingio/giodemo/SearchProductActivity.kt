package com.growingio.giodemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.growingio.giodemo.cart.defaultPrefs
import com.growingio.giodemo.cart.set
import kotlinx.android.synthetic.main.activity_search_product.*


class SearchProductActivity : AppCompatActivity(), View.OnClickListener {

    private var mInputMethodManager: InputMethodManager? = null
    private val context = this
    private val productKey = "product"

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product)

        mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        back.setOnClickListener(this)
        clear.setOnClickListener(this)
        search.setOnClickListener(this)
        et_search.setOnClickListener(this)
        btn_marker.setOnClickListener(this)
        btn_cup.setOnClickListener(this)
        btn_hacker_book.setOnClickListener(this)
        btn_book.setOnClickListener(this)
        btn_shirt.setOnClickListener(this)

        et_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Toast.makeText(this, "搜索 " + et_search.text, Toast.LENGTH_SHORT).show()
                //----GrowingIO SDK User Attention ! 为了采集 chng 事件（输入框输入内容事件)， 需要使得输入框失去焦点，才会采集
                et_search.isFocusable = false
            }
            false
        }

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    val result = goods.filter { it.name.contains(s.toString()) }
                    ll_goods_result.visibility = View.VISIBLE
                    ll_history_search.visibility = View.GONE
                    if (result.isNotEmpty()) {
                        img_not_found.visibility = View.GONE
                        rc_search_goods.visibility = View.VISIBLE
                        tv_search_result.text = "找到以下 " + result.size + " 个商品"
                        rc_search_goods.adapter = MyGoodsAdapter(context, result, false)
                        rc_search_goods.layoutManager = LinearLayoutManager(context)
                    } else {
                        img_not_found.visibility = View.VISIBLE
                        tv_search_result.text = "抱歉，未能找到您搜索的商品"
                        rc_search_goods.visibility = View.GONE
                    }
                } else {
                    ll_goods_result.visibility = View.GONE
                    ll_history_search.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onResume() {
        super.onResume()

        //----GrowingIO SDK User Attention ! 为了采集 chng 事件（输入框输入内容事件)， 需要使得输入框失去焦点，才会采集
        et_search.isFocusable = false
    }


    @SuppressLint("NewApi")
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back -> finish()
            R.id.et_search -> {
                et_search.isFocusableInTouchMode = true
                et_search.isFocusable = true
            }
            R.id.search -> {
                //----GrowingIO SDK User Attention ! 为了采集 chng 事件（输入框输入内容事件)， 需要使得输入框失去焦点，才会采集
                et_search.setFocusable(false)
                if (mInputMethodManager!!.isActive) {
                    mInputMethodManager!!.hideSoftInputFromWindow(et_search.windowToken, 0)
                }
            }
            R.id.clear -> et_search.setText("")
            R.id.btn_cup,
            R.id.btn_marker -> et_search.setText(gioCup.name)

            R.id.btn_hacker_book,
            R.id.btn_book -> et_search.setText(theHandBookOfGrowthHacker.name)
            R.id.btn_shirt -> et_search.setText(gioShirt.name)
        }
    }
}


class MyGoodsAdapter(context: Context, goods: List<Product>, isCartPage: Boolean) :
    RecyclerView.Adapter<MyGoodsViewHolder>() {

    private val context = context
    private val productData: List<Product> = goods
    private val isCartPageFragmet: Boolean = isCartPage

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGoodsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_home_search_product, parent, false)

        return MyGoodsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productData.size
    }

    override fun onBindViewHolder(holder: MyGoodsViewHolder, pisition: Int) {
        holder.goodsName.text = productData[pisition].name
        holder.goodsPrice.text = String.format("￥ ${productData[pisition].price}")
        holder.goodsImg.setImageResource(productData[pisition].categoryImg)
        holder.goodsImg.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    ProductDetailActivity::class.java
                ).putExtra("product", productData[pisition].name)
            )
        }
    }

}

class MyGoodsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val goodsName = itemView.findViewById<View>(R.id.goods_name) as TextView
    val goodsPrice = itemView.findViewById<View>(R.id.goods_price) as TextView
    val goodsImg = itemView.findViewById<View>(R.id.goods_img) as ImageView
}