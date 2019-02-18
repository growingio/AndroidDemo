package com.growingio.giodemo.profile

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import com.growingio.giodemo.R
import kotlinx.android.synthetic.main.activity_mine_order.*
import kotlinx.android.synthetic.main.include_page_title.*

class MyOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine_order)

        head_title.text = "我的订单"
        back.setOnClickListener { finish() }

        val title = arrayListOf<String>("全部", "待付款", "配送中", "已收货")
        val fragmentList =
            arrayListOf(
                AllOrderFragment.newInstance(),
                WaittingToPayFragment.newInstance(),
                DeliveringFragment.newInstance(),
                RecivedProductFragment.newInstance()
            )

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager_order.setCurrentItem(tab.position, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        val adapter = object : FragmentStatePagerAdapter(this.supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return title[position]
            }

            override fun getCount(): Int {
                return title.size
            }
        }

        viewpager_order.adapter = adapter

        var indicator = intent.getIntExtra("indicator", 0)
        viewpager_order.currentItem = indicator
        //这两个函数设置即可实现点击和滑动切换连动效果
        tab_layout.setupWithViewPager(viewpager_order)
    }
}
