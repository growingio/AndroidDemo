package com.growingio.androiddemo.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.growingio.androiddemo.R
import com.growingio.androiddemo.apiconfig.ShowAPIFragment
import com.growingio.androiddemo.checklist.CheckListFragment
import com.growingio.androiddemo.hybridsdk.HybridSDKPresentationFragment
import com.growingio.androiddemo.manualtrack.ManualTrackFragment
import com.growingio.androiddemo.questions.QuestionsFragment
import com.growingio.androiddemo.showfeature.ShowFeaturesFragment
import com.growingio.androiddemo.userstory.UserStoryFragment
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar_drawer.*

/**
 * classDesc: 首页
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val showFeatures = ShowFeaturesFragment()
    private val showAPIFragment = ShowAPIFragment()
    private val checkListFragment = CheckListFragment()
    private val userStoryFragment = UserStoryFragment()
    private val manualTrackFragment = ManualTrackFragment()
    private val hybridSDKPresentationFragment = HybridSDKPresentationFragment()
    private val questionsFragment = QuestionsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(bar)

        supportFragmentManager.beginTransaction().add(R.id.container, showFeatures).commit()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action\n" +
                    "Replace with your own action\n" +
                    "", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, bar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        val navMenu = nav_view.getChildAt(0) as NavigationMenuView
        navMenu.isVerticalScrollBarEnabled = false
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.nav_show_feature) {
            supportFragmentManager.beginTransaction().replace(R.id.container, showFeatures).commit()
        } else if (id == R.id.nav_config_api) {
            supportFragmentManager.beginTransaction().replace(R.id.container, showAPIFragment).commit()
        } else if (id == R.id.nav_manual_track) {
            supportFragmentManager.beginTransaction().replace(R.id.container, manualTrackFragment).commit()
        } else if (id == R.id.nav_hybrid_sdk) {
            supportFragmentManager.beginTransaction().replace(R.id.container, hybridSDKPresentationFragment).commit()
        } else if (id == R.id.nav_user_story) {
            supportFragmentManager.beginTransaction().replace(R.id.container, userStoryFragment).commit()
        } else if (id == R.id.nav_check_list) {
            supportFragmentManager.beginTransaction().replace(R.id.container, checkListFragment).commit()
        } else if (id == R.id.nav_questions) {
            supportFragmentManager.beginTransaction().replace(R.id.container, questionsFragment).commit()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
