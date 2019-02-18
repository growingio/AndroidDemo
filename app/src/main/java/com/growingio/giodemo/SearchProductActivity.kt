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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.growingio.android.sdk.collection.GrowingIO
import com.growingio.giodemo.cart.defaultPrefs
import com.growingio.giodemo.cart.set
import kotlinx.android.synthetic.main.activity_search_product.*
import org.json.JSONObject


class SearchProductActivity : AppCompatActivity(), View.OnClickListener {

    private var mInputMethodManager: InputMethodManager? = null
    private val context = this

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
                        tv_search_result.text = "找到以下 ${result.size} 个商品"
                        rc_search_goods.adapter = MyGoodsAdapter(context, result, s.toString(), null)
                        rc_search_goods.layoutManager = LinearLayoutManager(context)

                        //用户在搜索栏发起商品搜索
                        GrowingIO.getInstance().track("searchGoods", JSONObject().put(GioSearchKeyWord, s))

                        //用户搜索后返回结果是有搜索结果
                        GrowingIO.getInstance().track("searchResultView", JSONObject().put(GioSearchKeyWord, s))

                    } else {
                        img_not_found.visibility = View.VISIBLE
                        tv_search_result.text = "抱歉，未能找到您搜索的商品"
                        rc_search_goods.visibility = View.GONE

                        //用户在搜索栏发起商品搜索
                        GrowingIO.getInstance().track("searchGoods", JSONObject().put(GioSearchKeyWord, s))

                        //用户搜索后返回结果是无结果
                        GrowingIO.getInstance().track("searchNoResultView", JSONObject().put(GioSearchKeyWord, s))

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


class MyGoodsAdapter(
    context: Context,
    goods: List<Product>,
    private var searchKeyWords: String?,
    private var order_id: String?
) : RecyclerView.Adapter<MyGoodsViewHolder>() {
    private val context = context
    private val productData: List<Product> = goods

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
            if (searchKeyWords != null) {
                GrowingIO.getInstance().track(
                    "searchResultClick",
                    JSONObject()
                        .put(GioProductId, productData[pisition].id)
                        .put(GioProductName, productData[pisition].name)
                        .put(GioSearchKeyWord, searchKeyWords)
                )

            }
            context.startActivity(
                Intent(context, ProductDetailActivity::class.java)
                    .putExtra("product", productData[pisition].name)
            )

            //搜索词（转化变量）
            defaultPrefs(context)[productData[pisition].name + "search"] = searchKeyWords
        }

        if (order_id == null) {
            holder.order_id.visibility = View.GONE
        } else {
            holder.order_id.visibility = View.VISIBLE
            holder.order_id.text = "订单编号：$order_id"
        }
    }

}

class MyGoodsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val goodsName = itemView.findViewById<View>(R.id.goods_name) as TextView
    val goodsPrice = itemView.findViewById<View>(R.id.goods_price) as TextView
    val order_id = itemView.findViewById<View>(R.id.order_id) as TextView
    val goodsImg = itemView.findViewById<View>(R.id.goods_img) as ImageView
}