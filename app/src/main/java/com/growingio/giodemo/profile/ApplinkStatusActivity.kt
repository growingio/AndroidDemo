package com.growingio.giodemo.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.growingio.giodemo.R
import kotlinx.android.synthetic.main.activity_applink_status.*
import kotlinx.android.synthetic.main.include_page_title.*
import java.lang.Exception

class ApplinkStatusActivity : AppCompatActivity(){

    companion object{
        private val TAG = "AppLink"
    }

    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applink_status)
        head_title.text = "校验本App的AppLink状态"
        tvStatus = tv_status

        tvStatus.text = "日志从上往下看\n校验中..."
        Thread(this::check).start()
    }

    private fun check(){
        val appLinkIntent = Intent(Intent.ACTION_VIEW)
        appLinkIntent.data = Uri.parse("https://gio.ren/PQ2r6axidx")
        appLinkIntent.addCategory(Intent.CATEGORY_BROWSABLE)
        appLinkIntent.addCategory(Intent.CATEGORY_DEFAULT)

        val candidates = packageManager.queryIntentActivities(appLinkIntent, PackageManager.MATCH_DEFAULT_ONLY)
        Log.d(TAG, "candidates is $candidates")
        try {
            Thread.sleep(2000)
        }catch (e: InterruptedException){}
        if (candidates.size == 1){
            appendLog("校验通过, AppLink状态正常")
            return
        }else{
            appendLog("校验失败, AppLink校验失败")
            appendLog("开始校验当前IntentFilter校验组件")
        }
        try{
            Thread.sleep(2000)
        }catch (e: InterruptedException){}
        val intentFilterIntent = Intent("android.intent.action.INTENT_FILTER_NEEDS_VERIFICATION")
        val filterCandidates = packageManager.queryBroadcastReceivers(intentFilterIntent, PackageManager.MATCH_ALL)
        Log.d(TAG, "filterCandidates is $filterCandidates")
        try{
            val packageInfo = packageManager.getPackageInfo("com.google.android.gms", 0)
            Log.d(TAG, "packageInfo: $packageInfo")
            appendLog("发现com.google.android.gms")
            appendLog("该服务如果网络不通，校验将失败")
        }catch (e: Exception){
            Log.e(TAG, e.message, e)
            appendLog("com.google.android.gms服务组件未发现")
            appendLog("使用的是com.android.statementservice服务")
            appendLog("!!!!请校验签名与包名一致!!!!")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun appendLog(log: String){
        runOnUiThread {
            tvStatus.text = "${tvStatus.text}\n$log"
        }
    }
}