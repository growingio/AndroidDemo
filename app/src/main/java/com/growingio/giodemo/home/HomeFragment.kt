package com.growingio.giodemo.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.growingio.android.sdk.collection.GrowingIO
import com.growingio.giodemo.*
import com.growingio.giodemo.profile.MyOrderActivity
import org.json.JSONObject
import java.lang.ref.WeakReference

class HomeFragment : Fragment(), View.OnClickListener {
    object HomeFragment

    private val productKey = "product"
    private var listener: OnFragmentInteractionListener? = null
    private var bannerPager: ViewPager? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        bannerPager = view.findViewById<View>(R.id.banner) as ViewPager

        var scrollTask: BannerScrollTask = BannerScrollTask(bannerPager!!)

        bannerPager!!.adapter = MyPagerAdapter(activity as Context?, bannerPager!!)
        bannerPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(p0: Int) {

                if (userVisibleHint && isResumed) {

                    // start
                    when (p0) {
                        1 -> GrowingIO.getInstance().track(
                            "homePageGoodsImp", JSONObject()
                                .put(GioProductId, gioCup.id)
                                .put(GioAdPosition, "banner")
                                .put(GioProductName, gioCup.name)
                        )

                        2 -> GrowingIO.getInstance().track(
                            "homePageGoodsImp", JSONObject()
                                .put(GioProductId, gioShirt.id)
                                .put(GioAdPosition, "banner")
                                .put(GioProductName, gioShirt.name)
                        )
                    }
                } else {

                    bannerPager!!.removeCallbacks(scrollTask)
                }

            }
        })

        val search = view.findViewById<View>(R.id.search)
        search.setOnClickListener(this)

        view.findViewById<View>(R.id.limited1).setOnClickListener(this)
        view.findViewById<View>(R.id.limited2).setOnClickListener(this)
        view.findViewById<View>(R.id.limited3).setOnClickListener(this)
        view.findViewById<View>(R.id.category1).setOnClickListener(this)
        view.findViewById<View>(R.id.category2).setOnClickListener(this)
        view.findViewById<View>(R.id.category3).setOnClickListener(this)
        view.findViewById<View>(R.id.category4).setOnClickListener(this)
        view.findViewById<View>(R.id.img_suggested).setOnClickListener(this)

        scrollTask.run()

        return view
    }

    override fun onClick(v: View?) {
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
            R.id.category4 -> startActivity(Intent(activity, MyOrderActivity::class.java).putExtra("indicator", 1))

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

                viewPager.postDelayed(this, 2000)
            }
        }

    }

}


