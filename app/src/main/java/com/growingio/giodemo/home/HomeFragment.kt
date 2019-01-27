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
import com.growingio.giodemo.*

class HomeFragment : Fragment(), View.OnClickListener {
    private val productKey = "product"
    private var listener: OnFragmentInteractionListener? = null
    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val bannerPager: ViewPager = view.findViewById<View>(R.id.banner) as ViewPager
        bannerPager.adapter = MyPagerAdapter(activity)

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

        return view
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.search -> startActivity(Intent(activity, SearchProductActivity::class.java))
            R.id.limited1 -> startActivity(
                Intent(activity, ProductDetailActivity::class.java)
                    .putExtra(
                        productKey,
                        theHandBookOfGrowthHacker.name
                    )
            )
            R.id.limited2 -> startActivity(
                Intent(activity, ProductDetailActivity::class.java)
                    .putExtra(
                        productKey,
                        theHandBookOfDataOperation.name
                    )
            )
            R.id.limited3 -> startActivity(
                Intent(activity, ProductDetailActivity::class.java)
                    .putExtra(
                        productKey,
                        theHandBookOfPMAnalytics.name
                    )
            )
            R.id.img_suggested -> startActivity(
                Intent(activity, ProductDetailActivity::class.java)
                    .putExtra(
                        productKey,
                        gioShirt.name
                    )
            )
            R.id.category1, R.id.category2 -> listener!!.onFragmentInteraction(1)
            R.id.category3 -> listener!!.onFragmentInteraction(2)

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
            imageView.setOnClickListener {
                when (position) {
                    0 -> context.startActivity(
                        Intent(context, ProductDetailActivity::class.java)
                            .putExtra(
                                "product",
                                gioCup.name
                            )
                    )
                    1 -> context.startActivity(
                        Intent(context, ProductDetailActivity::class.java)
                            .putExtra(
                                "product",
                                gioShirt.name
                            )
                    )
                }
            }
            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(tabIndicator: Int)
    }


}

