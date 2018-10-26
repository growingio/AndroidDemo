package com.growingio.androiddemo.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.growingio.androiddemo.R

class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        val toolbar = findViewById<View>(R.id.bar) as BottomAppBar
        setSupportActionBar(toolbar)

        val toFragment = ToFragment()
        supportFragmentManager.beginTransaction().add(R.id.container,toFragment).commit()

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action\n" +
                    "Replace with your own action\n" +
                    "Replace with your own action\n" +
                    "Replace with your own action\n" +
                    "Replace with your own action\n" +
                    "Replace with your own action\n" +
                    "Replace with your own action\n" +
                    "Replace with your own action\n" +
                    "Replace with your own action\n" +
                    "Replace with your own action\n" +
                    "Replace with your own action\n" +
                    "", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
//
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val navMenu = navigationView.getChildAt(0) as NavigationMenuView
        navMenu.isVerticalScrollBarEnabled = false
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_show_feature) {

        } else if (id == R.id.nav_config_api) {

        } else if (id == R.id.nav_manual_track) {
        } else if (id == R.id.nav_hybrid_sdk) {
        } else if (id == R.id.nav_user_story) {
        } else if (id == R.id.nav_check_list) {
        } else if (id == R.id.nav_questions) {

        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
