package com.growingio.giodemo.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.growingio.android.sdk.collection.GrowingIO
import com.growingio.giodemo.R
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.item_profile.view.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.rc_profile.adapter = MyProfileAdapter(activity as Context)
        view.rc_profile.layoutManager = LinearLayoutManager(activity as Context)
        return view
    }

}


class MyProfileAdapter(context: Context) : RecyclerView.Adapter<MyViewHolder>() {
    private val context = context
    private val profileInfo = arrayListOf(
        ProfileInfo("积分", "5799"),
        ProfileInfo("会员等级", "金牌"),
        ProfileInfo("我的订单", activity = MyOrderActivity::class.java),
        ProfileInfo("红包", "￥ 120"),
        ProfileInfo("地址", "北京 望京 宝能中心B座"),
        ProfileInfo("获取调试信息", activity = GetDebugInfoActivity::class.java),
        ProfileInfo("校验AppLink状态", activity = ApplinkStatusActivity::class.java)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return profileInfo.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val info = profileInfo[position]
        holder.title.text = info.title
        holder.content.text = info.content
        holder.arrow.visibility = if (info.content == null) View.VISIBLE else View.GONE

        if (info.activity != null){
            holder.root.setOnClickListener {
                if (info.activity == MyOrderActivity::class.java){
                    // 我的订单
                    GrowingIO.getInstance().setPeopleVariable("isUserSubmitOdrer", "true")
                }
                context.startActivity(Intent(context, info.activity))
            }
        }else{
            holder.root.setOnClickListener(null)
        }
    }

}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title = itemView.tv_title!!
    val content = itemView.tv_content!!
    val arrow = itemView.img_arrow_right!!
    val root = itemView.root!!

}

data class ProfileInfo(val title: String, val content: String? = null, val activity: Class<out Activity>? = null)