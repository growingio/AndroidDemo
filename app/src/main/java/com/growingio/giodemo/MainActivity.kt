package com.growingio.giodemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.growingio.giodemo.cart.ShoppingCartFragment
import com.growingio.giodemo.category.CategoryFragment
import com.growingio.giodemo.home.HomeFragment
import com.growingio.giodemo.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var menuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragments = ArrayList<Fragment>()
        val homeFragment = HomeFragment.newInstance()
        val shoppingCartFragment = ShoppingCartFragment.newInstance()
        val profileFragment = ProfileFragment.newInstance()
        val categoryFragment = CategoryFragment.newInstance()

        fragments.add(homeFragment)
        fragments.add(categoryFragment)
        fragments.add(shoppingCartFragment)
        fragments.add(profileFragment)

        viewpager_main.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return fragments.size
            }
        }

        viewpager_main.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(position: Int) {
                if (menuItem != null) {
                    menuItem!!.isChecked = false
                } else {
                    navigation.menu.getItem(0).isChecked = false
                }
                menuItem = navigation.menu.getItem(position)
                menuItem!!.isChecked = true
            }

        })

        navigation.setOnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    viewpager_main.currentItem = 0
                    true
                }
                R.id.category -> {
                    viewpager_main.currentItem = 1
                    true
                }
                R.id.cart -> {

                    viewpager_main.currentItem = 2
                    true
                }
                R.id.profile -> {

                    viewpager_main.currentItem = 3
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}
