package com.growingio.androiddemo.showfeature

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.growingio.androiddemo.R
import kotlinx.android.synthetic.main.fragment_show_features.*
import java.util.*

/**
 * classDesc: 功能展示
 */

class ShowFeaturesFragment : Fragment(), ViewPager.OnPageChangeListener {


    private val bannerArray: IntArray = intArrayOf(
            R.mipmap.banner1_simon,
            R.mipmap.banner2_simon2,
            R.mipmap.banner4_zhihu1,
            R.mipmap.banner5_zhihu2)
    private val imageDescription = arrayOf(
            "用数据驱动企业增长",
            "如何低成本实现爆发式增长",
            "如何低成本实现爆发式增长",
            "Hubspot的亿万美元增长之路")

    private var mImageViews: MutableList<ImageView>? = null
    private var preEnablePosition: Int = 0
    private val sLocationOnScreenBuffer = IntArray(2)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_show_features, container, false)
        val ll_banner_docs = view.findViewById<View>(R.id.ll_banner_docs) as LinearLayout
        val banner_desc = view.findViewById<View>(R.id.banner_desc) as TextView
        val banner = view.findViewById<View>(R.id.banner) as ViewPager
        val recycler_to = view.findViewById<View>(R.id.recycler_to) as RecyclerView


        mImageViews = ArrayList<ImageView>() as MutableList<ImageView>?
        var iv: ImageView
        var params: LinearLayout.LayoutParams
        for (id in bannerArray) {
            iv = ImageView(activity)
            iv.setBackgroundResource(id)
            (mImageViews as ArrayList<ImageView>).add(iv)
            val bannerView = View(activity)
            bannerView.setBackgroundResource(R.drawable.point_background)
            params = LinearLayout.LayoutParams(15, 15)
            params.leftMargin = 5
            bannerView.isEnabled = false
            bannerView.layoutParams = params
            ll_banner_docs.addView(bannerView)
            iv.setOnClickListener { v -> Log.i("GIO.ToFragment", "onClick: 点击banner图片 $v") }
        }

        banner.adapter = BannerAdapter(mImageViews)
        banner.addOnPageChangeListener(this)
        banner_desc.text = imageDescription[0]
        ll_banner_docs.getChildAt(0).isEnabled = true

        banner.currentItem = 0

        // 列表初始化
        recycler_to.setHasFixedSize(true)
        recycler_to.layoutManager = LinearLayoutManager(activity)
        recycler_to.adapter = ToAdapter(activity as Context)

        return view
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        for (i in 0 until banner!!.getChildCount()) {
            val view = banner!!.getChildAt(i)
            view.getLocationOnScreen(sLocationOnScreenBuffer)
        }
    }

    override fun onPageSelected(position: Int) {
        val newPosition = position % bannerArray.size
        banner_desc!!.text = imageDescription[newPosition]
        ll_banner_docs!!.getChildAt(preEnablePosition).isEnabled = false
        ll_banner_docs!!.getChildAt(newPosition).isEnabled = true
        preEnablePosition = newPosition
    }


}


class ToAdapter(private val context: Context) : RecyclerView.Adapter<ToViewHolder>() {

//    val demoList:List<DemoBean> =
//    , private val list: List<DemoBean>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToViewHolder {
        val toViewHolder = ToViewHolder(LayoutInflater.from(context).inflate(R.layout.item_content, parent, false))


        return toViewHolder
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ToViewHolder, position: Int) {
        holder.item_title.text = "haha"
        holder.item_subtitle.text = "sss"

    }


}

class ToViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val item_title: TextView = itemView.findViewById(R.id.item_title)
    val item_subtitle: TextView = itemView.findViewById(R.id.item_subtitle)
    val item_feature_desc: TextView = itemView.findViewById(R.id.item_feature_desc)
    val item_icon: ImageView = itemView.findViewById(R.id.item_icon)
}

class BannerAdapter(private val mImageViews: MutableList<ImageView>?) : PagerAdapter() {

    override fun destroyItem(@NonNull container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(mImageViews!![position])
    }


    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        container.addView(mImageViews!![position])
        return mImageViews[position]
    }

    override fun getCount(): Int {
        return mImageViews!!.size
    }


    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        // TODO Auto-generated method stub
        return arg0 === arg1
    }

}