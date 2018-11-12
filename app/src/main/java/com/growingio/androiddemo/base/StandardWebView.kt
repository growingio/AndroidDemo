package com.growingio.androiddemo.base

import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.growingio.androiddemo.R
import kotlinx.android.synthetic.main.activity_standard_web_view.*

class StandardWebView : AppCompatActivity() {
    private var jsCallManager: JSCallManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_standard_web_view)

        initWeb()

        val url = intent.getStringExtra("url")

        if (TextUtils.isEmpty(url)) {
            webview.loadUrl("file:///android_asset/gio_hybrid.html")
            //            webview.loadUrl("http://www.hybrid.com/");
        } else {
            webview.loadUrl(url)
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initWeb() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progress_bar.progress = newProgress
                if (newProgress == 100) {
                    progress_bar.visibility = View.GONE
                } else {
                    progress_bar.visibility = View.VISIBLE
                }
                super.onProgressChanged(view, newProgress)
            }

        }


        webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                try {
                    if (url!!.startsWith("growing")) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()))
                        startActivity(intent)
                    }
                } catch (e: Exception) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                }
                super.onPageStarted(view, url, favicon)
            }
        }

        //声明WebSettings子类
        val webSettings = webview.settings

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.javaScriptEnabled = true


        //设置自适应屏幕，两者合用
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = false //隐藏原生的缩放控件

        //其他细节操作
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存
        webSettings.allowFileAccess = true //设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8"//设置编码格式

        //不使用缓存:
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE

        //这段代码在初使化webview时调用
        //JSCallManager本地处理js消息的对象
        if (Build.VERSION.SDK_INT >= 17) {
            // 在sdk4.2以上的系统上继续使用addJavascriptInterface
            webview.addJavascriptInterface(JSCallManager(), "Native")
        } else {
            //4.2之前 addJavascriptInterface有安全泄漏风险
            //移除js中的searchBoxJavaBridge_对象,在Android 3.0以下，系统自己添加了一个叫
            //searchBoxJavaBridge_的Js接口，要解决这个安全问题，我们也需要把这个接口删除
            jsCallManager = JSCallManager()
            webview.removeJavascriptInterface("searchBoxJavaBridge_")
        }
    }

    class JSCallManager {
        val type: String
            @JavascriptInterface
            get() = "Hello World"

        fun call(methodName: String, value: String): Any? {
            return null
        }
    }
}
