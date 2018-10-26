package com.growingio.androiddemo.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.growingio.androiddemo.R

/**
 * classDesc: 首屏页面的框架
 */

class ToFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_to, container, false)

        val recycler_to = view.findViewById<View>(R.id.recycler_to) as RecyclerView
        recycler_to.setHasFixedSize(true)
        recycler_to.layoutManager = LinearLayoutManager(activity)

        recycler_to.adapter = ToAdapter(activity as Context)
        return view
    }

}


class ToAdapter(private val context: Context) : RecyclerView.Adapter<ToViewHolder>() {

//    val demoList:List<>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToViewHolder {
        val toViewHolder = ToViewHolder(LayoutInflater.from(context).inflate(R.layout.item_content, parent, false))


        return toViewHolder
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ToViewHolder, position: Int) {
        holder.item_title.text = "haha"
        holder.item_subtitle.text = "haha"

    }


}

class ToViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val item_title: TextView = itemView.findViewById(R.id.item_title)
    val item_subtitle: TextView = itemView.findViewById(R.id.item_subtitle)
    val item_feature_desc: TextView = itemView.findViewById(R.id.item_feature_desc)
    val item_icon: ImageView = itemView.findViewById(R.id.item_icon)
}