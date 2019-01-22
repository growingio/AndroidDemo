package com.growingio.giodemo.profile

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

class ProfileFragment : Fragment() {

    private var recyclerView: RecyclerView? = null

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        recyclerView = view.findViewById<View>(R.id.rc_profile) as RecyclerView
        recyclerView!!.adapter = MyProfileAdapter(activity as Context)
        recyclerView!!.layoutManager = LinearLayoutManager(activity as Context)
        return view
    }

}


class MyProfileAdapter(context: Context) : RecyclerView.Adapter<MyViewHolder>() {
    private val context = context
    private val profileInfo: List<ProfileInfo> = arrayListOf(
        ProfileInfo("积分", "5799"), ProfileInfo("会员等级", "金牌"),
        ProfileInfo("我的订单", ""), ProfileInfo("红包", "￥ 120"),
        ProfileInfo("地址", "北京 望京 宝能中心B座")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return profileInfo.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, p1: Int) {
        holder.title.text = profileInfo[p1].title
        holder.content.text = profileInfo[p1].content
    }

}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title = itemView.findViewById<View>(R.id.tv_title) as TextView
    val content = itemView.findViewById<View>(R.id.tv_content) as TextView

}

data class ProfileInfo(val title: String, val content: String)