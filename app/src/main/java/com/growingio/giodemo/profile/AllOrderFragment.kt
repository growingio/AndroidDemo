package com.growingio.giodemo.profile

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.growingio.giodemo.*

class AllOrderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_order, container, false)
        val recyclerViewAll = view.findViewById<View>(R.id.rc_all) as RecyclerView

        recyclerViewAll.adapter = MyOrdersAdapter(activity!!)
        recyclerViewAll.layoutManager = LinearLayoutManager(activity)
        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() = AllOrderFragment()
    }
}

class MyOrdersAdapter(private val context: Context) :
    RecyclerView.Adapter<MyOrdersViewHolder>() {

    private val productData: List<Product> = arrayListOf(gioCup, gioShirt, theHandBookOfGrowthHacker)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)

        return MyOrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productData.size
    }

    override fun onBindViewHolder(holder: MyOrdersViewHolder, position: Int) {
        holder.goodsName.text = productData[position].name
        holder.goodsPrice.text = String.format("￥ ${productData[position].price}")
        holder.goodsImg.setImageResource(productData[position].categoryImg)
        holder.goodsNum.text = String.format("订单编号：${productData[position].orderNumber}")
        holder.goodsStatus.text = productData[position].status
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            when (position) {
                0 -> holder.goodsStatus.background = context.getDrawable(R.drawable.bg_orange)
                1 -> holder.goodsStatus.background = context.getDrawable(R.drawable.bg_yellow)
                2 -> holder.goodsStatus.background = context.getDrawable(R.drawable.bg_gray)
            }
        }

        holder.goodsImg.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    ProductDetailActivity::class.java
                ).putExtra("product", productData[position].name)
            )
        }
    }

}

class MyOrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val goodsNum = itemView.findViewById<View>(R.id.tv_pd_num) as TextView
    val goodsName = itemView.findViewById<View>(R.id.tv_pd_name) as TextView
    val goodsPrice = itemView.findViewById<View>(R.id.tv_pd_price) as TextView
    val goodsStatus = itemView.findViewById<View>(R.id.btn_status) as Button
    val goodsImg = itemView.findViewById<View>(R.id.img_product_icon) as ImageView
}