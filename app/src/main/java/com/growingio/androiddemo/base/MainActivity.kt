package com.growingio.androiddemo.base

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
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
    private var popWindow: PopupWindow? = null
    private var screenWidth = 0
    private var screenHeight = 0
    private var snackbar: Snackbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(bar)

        supportFragmentManager.beginTransaction().add(R.id.container, showFeatures).commit()

        snackbar = Snackbar.make(fab, "", Snackbar.LENGTH_INDEFINITE)
        val layout = snackbar!!.view as Snackbar.SnackbarLayout
        layout.removeAllViews()
        val dataView = LayoutInflater.from(this).inflate(R.layout.layout_show_collect_data, null, false)
        val collectData = dataView.findViewById<View>(R.id.collect_data) as TextView
        collectData.text = "hhhhh"
        layout.addView(dataView)

        fab.setOnClickListener { view ->

            if (snackbar!!.isShown) {
                snackbar!!.dismiss()
            } else {
                snackbar!!.show()
            }
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


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        screenWidth = windowManager.defaultDisplay.width
        screenHeight = windowManager.defaultDisplay.height
        popWindow = PopupWindow(screenWidth, screenHeight - 300)
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
        return if (id == R.id.menu_scan) {
            val integrator = IntentIntegrator(this)
            integrator.setOrientationLocked(false)
            integrator.captureActivity = CustomCaptureActivity::class.java
            integrator.initiateScan()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val mClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("gio_scan", result.contents)
                mClipboardManager.primaryClip = clipData
                startActivity(Intent(this, StandardWebView::class.java).putExtra("url", result.contents))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (snackbar!!.isShown) {
            snackbar!!.dismiss()
            return true
        } else {
            return false

        }
    }
}
