package com.growingio.giodemo.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import com.growingio.giodemo.*

class CategoryFragment : Fragment(), View.OnClickListener {

    val productKey = "product"

    companion object {
        fun newInstance() = CategoryFragment()

        // 为你推荐
        val suggestions = listOf(
            CategoryItem(listOf(gioCup, theHandBookOfGrowthHacker), "热门推荐"),
            CategoryItem(listOf(theHandBookOfDataOperation, theHandBookOfPMAnalytics), "电子书"),
            CategoryItem(listOf(gioShirt, gioCup), "生活用品")
        )
        // 电子书
        val eBooks = listOf(
            CategoryItem(listOf(theHandBookOfGrowthHacker, theHandBookOfDataOperation), "热门电子书")
        )
        // 生活用品
        val supplies = listOf(
            CategoryItem(listOf(gioCup, gioShirt), "热门商品")
        )
        // 衣服
        val cloth = listOf(
            CategoryItem(listOf(gioShirt), "Simmon同款帽衫")
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        val productRecyclerView: RecyclerView = view.findViewById<View>(R.id.product) as RecyclerView

        productRecyclerView.adapter = MyProductAdapter(activity as Context, suggestions)
        productRecyclerView.layoutManager = LinearLayoutManager(activity as Context)

        view.findViewById<View>(R.id.search_bar).setOnClickListener(this)
        view.findViewById<View>(R.id.img_banner).setOnClickListener(this)
        val radioGroup = view.findViewById<View>(R.id.radio_group) as RadioGroup
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_btn1 -> productRecyclerView.adapter = MyProductAdapter(activity as Context, suggestions)
                R.id.radio_btn2 -> productRecyclerView.adapter = MyProductAdapter(activity as Context, eBooks)
                R.id.radio_btn3 -> productRecyclerView.adapter = MyProductAdapter(activity as Context, supplies)
                R.id.radio_btn4 -> productRecyclerView.adapter = MyProductAdapter(activity as Context, cloth)
            }

        }

        return view
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.search_bar -> startActivity(Intent(activity, SearchProductActivity::class.java))
            R.id.img_banner -> startActivity(
                Intent(activity, ProductDetailActivity::class.java)
                    .putExtra(
                        productKey,
                        gioShirt.name
                    )
            )
        }
    }
}


class MyProductAdapter(context: Context, list: List<CategoryItem>) : RecyclerView.Adapter<MyViewHolder>() {


    private val context = context
    private val list = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, p1: Int) {
        holder.title.text = list[p1].category
        holder.itemLeft.setImageResource(list[p1].products[0].categoryImg)
        holder.itemLeft.setOnClickListener {
            context.startActivity(
                Intent(context, ProductDetailActivity::class.java).putExtra(
                    "product",
                    list[p1].products[0].name
                )
            )
        }
        if (list[p1].products.size > 1) {
            holder.itemRight.setImageResource(list[p1].products[1].categoryImg)
            holder.itemRight.setOnClickListener {
                context.startActivity(
                    Intent(context, ProductDetailActivity::class.java).putExtra(
                        "product",
                        list[p1].products[1].name
                    )
                )
            }
        }

    }

}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title = itemView.findViewById<View>(R.id.item_title) as TextView
    val itemLeft = itemView.findViewById<View>(R.id.hot1) as ImageView
    val itemRight = itemView.findViewById<View>(R.id.hot2) as ImageView

}

data class CategoryItem(val products: List<Product>, val category: String)