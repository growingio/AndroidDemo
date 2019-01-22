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
import com.growingio.giodemo.ProductDetailActivity
import com.growingio.giodemo.R
import com.growingio.giodemo.SearchProductActivity

class HomeFragment : Fragment(), View.OnClickListener {


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val bannerPager: ViewPager = view.findViewById<View>(R.id.banner) as ViewPager
        bannerPager.adapter = MyPagerAdapter(context)
        val search = view.findViewById<View>(R.id.search)
        search.setOnClickListener(this)

        view.findViewById<View>(R.id.limited1).setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.search -> startActivity(Intent(activity, SearchProductActivity::class.java))
            R.id.limited1 -> startActivity(Intent(activity, ProductDetailActivity::class.java))
        }
    }

    class MyPagerAdapter(mycontext: Context?) : PagerAdapter() {

        var context: Context = mycontext!!

        private var imageList = mutableListOf(R.drawable.banner1, R.drawable.banner2)

        override fun isViewFromObject(p0: View, p1: Any): Boolean {
            return p0 == p1
        }

        override fun getCount(): Int {
            return imageList.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var imageView = ImageView(context)
            imageView.setImageResource(imageList[position])
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            container.addView(imageView)
            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }


}

