package com.growingio.giodemo.category

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.growingio.giodemo.R

class CategoryFragment : Fragment() {


    companion object {
        fun newInstance() = CategoryFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        val productRecyclerView: RecyclerView = view.findViewById<View>(R.id.product) as RecyclerView
        productRecyclerView.adapter = MyProductAdapter(activity as Context)
        productRecyclerView.layoutManager = LinearLayoutManager(activity as Context)

        return view
    }
}


class MyProductAdapter(context: Context) : RecyclerView.Adapter<MyViewHolder>() {
    private val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: MyViewHolder, p1: Int) {
        holder.item_title.text = "大促销啊"
    }

}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val item_title = itemView.findViewById<View>(R.id.item_title) as TextView

}