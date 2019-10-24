package com.growingio.giodemo.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.growingio.android.sdk.collection.GrowingIO
import com.growingio.giodemo.*
import com.growingio.giodemo.profile.MyOrderActivity
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONObject
import java.lang.ref.WeakReference
import com.growingio.android.sdk.gtouch.widget.banner.GTouchBanner
import com.growingio.android.sdk.gtouch.widget.banner.listener.BannerStateChangedListener


class HomeFragment : Fragment(), View.OnClickListener {
    object HomeFragment

    private lateinit var gtouch_banner: GTouchBanner
    private val productKey = "product"
    private var listener: OnFragmentInteractionListener? = null
    private var count = 0
    private lateinit var mSpHelper: SpHelper


    // tab 页面之间跳转，通过 activity 通信
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onResume() {
        super.onResume()
        mSpHelper = SpHelper(context)

        gtouch_banner.loadData(object :BannerStateChangedListener{
            /**
             * Banner数据加载成功
             *
             * @param banner Banner控件对象
             */
            override fun onLoadDataSuccess(banner:GTouchBanner) {
                Log.e(TAG, "onLoadDataSuccess: ")
            }


            /**
             * Banner数据加载失败
             *
             * @param banner Banner控件对象
             * @param errorCode 错误码
             * @param description 错误描述，有可能为null
             */
            override fun onLoadDataFailed( banner:GTouchBanner, errorCode:Int,  description:String) {
                Log.e(TAG, "onLoadDataFailed: errorCode = " + errorCode + ", description = " + description);
            }


            /**
             * Banner item被点击
             *
             * @param banner Banner控件对象
             * @param position item所处位置
             * @param targetUrl 需要跳转的路由url
             * @return 返回为true，触达SDK不在处理跳转的路由url；返回为false，触达SDK会处理跳转内部界面和H5两种触达系统提供的路由
             */
            override fun onItemClick(banner:GTouchBanner, position:Int, targetUrl:String):Boolean{
                Log.e(TAG, "onItemClick: position = " + position + ", targetUrl = " + targetUrl);
                return false
            }

            /**
             * 加载失败图片被点击
             *
             * @param banner Banner控件对象
             */
            override fun onErrorImageClick( banner:GTouchBanner) {
                Log.e(TAG, "onErrorImageClick")
            }
        }
        )


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mSpHelper = SpHelper(context)
        gtouch_banner = view.gtouch_banner
        gtouch_banner.loadData(object: BannerStateChangedListener {
            /**
             * Banner数据加载成功
             *
             * @param banner Banner控件对象
             */
            override fun onLoadDataSuccess(banner:GTouchBanner) {
                Log.e(TAG, "onLoadDataSuccess: ")
            }


            /**
             * Banner数据加载失败
             *
             * @param banner Banner控件对象
             * @param errorCode 错误码
             * @param description 错误描述，有可能为null
             */
            override fun onLoadDataFailed( banner:GTouchBanner, errorCode:Int,  description:String) {
                Log.e(TAG, "onLoadDataFailed: errorCode = " + errorCode + ", description = " + description);
            }


            /**
             * Banner item被点击
             *
             * @param banner Banner控件对象
             * @param position item所处位置
             * @param targetUrl 需要跳转的路由url
             * @return 返回为true，触达SDK不在处理跳转的路由url；返回为false，触达SDK会处理跳转内部界面和H5两种触达系统提供的路由
             */
            override fun onItemClick(banner:GTouchBanner, position:Int, targetUrl:String):Boolean{
                Log.e(TAG, "onItemClick: position = " + position + ", targetUrl = " + targetUrl);
                return false
            }

            /**
             * 加载失败图片被点击
             *
             * @param banner Banner控件对象
             */
            override fun onErrorImageClick( banner:GTouchBanner) {
                Log.e(TAG, "onErrorImageClick")
            }
        }
        )



        view.search.setOnClickListener(this)
        view.limited1.setOnClickListener(this)
        view.limited2.setOnClickListener(this)
        view.limited3.setOnClickListener(this)
        view.img_suggested.setOnClickListener(this)
        view.category1.setOnClickListener(this)
        view.category2.setOnClickListener(this)
        view.category3.setOnClickListener(this)
        view.category4.setOnClickListener(this)

        GrowingIO.setViewContent(view.limited1, "增长黑客手册")
        GrowingIO.setViewContent(view.limited2, "数据运营手册")
        GrowingIO.setViewContent(view.limited3, "产品经理数据分析")
        GrowingIO.setViewContent(view.img_suggested, "GIO 帽衫")

        //view绘制后，查看当前流量位的展示，发送自定义事件
        trackAdPosition(view.limited1, "限时秒杀", theHandBookOfGrowthHacker)
        trackAdPosition(view.limited2, "限时秒杀", theHandBookOfDataOperation)
        trackAdPosition(view.limited3, "限时秒杀", theHandBookOfPMAnalytics)
        trackAdPosition(view.img_suggested, "GIO推荐", gioShirt)

        //上下滚动，检测流量位是否有展示，每次消失再展示，都发送一个自定义事件
        view.scroll_view.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
            // 广告流量为展现次数打点 ~ 判断当前 view 是否在屏幕上
            trackAdPosition(view.limited1, "限时秒杀", theHandBookOfGrowthHacker)
            trackAdPosition(view.limited2, "限时秒杀", theHandBookOfDataOperation)
            trackAdPosition(view.limited3, "限时秒杀", theHandBookOfPMAnalytics)
            trackAdPosition(view.img_suggested, "GIO推荐", gioShirt)
        })

        return view
    }

    override fun onClick(v: View?) {
        mSpHelper = SpHelper(context)
        when (v!!.id) {
            R.id.search -> startActivity(Intent(activity, SearchProductActivity::class.java))
            R.id.limited1 -> startActivity(
                Intent(activity, ProductDetailActivity::class.java)
                    .putExtra(productKey, theHandBookOfGrowthHacker.name)
                    .putExtra("evar", EVAR_LIMITED)
            )
            R.id.limited2 -> startActivity(
                Intent(activity, ProductDetailActivity::class.java)
                    .putExtra(productKey, theHandBookOfDataOperation.name)
                    .putExtra("evar", EVAR_LIMITED)
            )
            R.id.limited3 -> startActivity(
                Intent(activity, ProductDetailActivity::class.java)
                    .putExtra(productKey, theHandBookOfPMAnalytics.name)
                    .putExtra("evar", EVAR_LIMITED)
            )
            R.id.img_suggested -> startActivity(
                Intent(activity, ProductDetailActivity::class.java)
                    .putExtra(productKey, gioShirt.name)
                    .putExtra("evar", EVAR_SUGGESTED)
            )
            R.id.category1, R.id.category2 -> listener!!.onFragmentInteraction(1)
            R.id.category3 -> listener!!.onFragmentInteraction(2)
            R.id.category4 -> {
                count += 1
                mSpHelper.saveImgOpenCnt(count)
                GrowingIO.getInstance().setPeopleVariable("isUserSubmitOdrer",mSpHelper.getImgOpenCnt())
                startActivity(Intent(activity, MyOrderActivity::class.java).putExtra("indicator", 1))}

        }
    }

    class MyPagerAdapter(mycontext: Context?, private var viewPager: ViewPager) : PagerAdapter() {

        var context: Context = mycontext!!

        private var imageList = mutableListOf(R.drawable.banner1, R.drawable.banner2)

        override fun isViewFromObject(p0: View, p1: Any): Boolean {
            return p0 == p1
        }

        override fun getCount(): Int {
            return imageList.size + 2
        }

        override fun instantiateItem(container: ViewGroup, p: Int): Any {
            var position: Int = p % imageList.size

            var imageView = ImageView(context)
            imageView.setImageResource(imageList[position])
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            container.addView(imageView)
            imageView.setOnClickListener {
                when (position) {
                    0 -> {
                        GrowingIO.getInstance().track(
                            "homePageGoodsClick",
                            JSONObject()
                                .put(GioProductId, gioCup.id)
                                .put(GioAdPosition, "banner")
                                .put(GioProductName, gioCup.name)
                        )
                        context.startActivity(
                            Intent(context, ProductDetailActivity::class.java)
                                .putExtra("product", gioCup.name)
                                .putExtra("evar", EVAR_BANNER)
                        )
                    }
                    1 -> {
                        GrowingIO.getInstance().track(
                            "homePageGoodsClick",
                            JSONObject()
                                .put(GioProductId, gioShirt.id)
                                .put(GioAdPosition, "banner")
                                .put(GioProductName, gioShirt.name)
                        )
                        context.startActivity(
                            Intent(context, ProductDetailActivity::class.java)
                                .putExtra("product", gioShirt.name)
                                .putExtra("evar", EVAR_BANNER)

                        )
                    }
                }
            }
            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View)
        }


        override fun finishUpdate(container: ViewGroup) {
            var position = viewPager.currentItem
            if (position == 0) {
                position = imageList.size
                viewPager.setCurrentItem(position, false)
            } else if (position == imageList.size + 2 - 1) {
                position = 1
                viewPager.setCurrentItem(position, false)
            }
        }
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(tabIndicator: Int)
    }

    class BannerScrollTask(private val viewPager: ViewPager) : Runnable {

        private val weakReference: WeakReference<HomeFragment> = WeakReference(HomeFragment)

        override fun run() {
            val homeFrag: HomeFragment? = weakReference.get()
            if (homeFrag != null) {
                var pagePosition = viewPager.currentItem + 1
                viewPager.setCurrentItem(pagePosition, true)
                viewPager.postDelayed(this, 5000)
            }
        }

    }

}
