package com.growingio.androiddemo.base

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.growingio.androiddemo.R
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class StandardWebView : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST = 2
    internal var webView: WebView? = null
    internal var jsCallManager: JSCallManager? = null
    internal var progressBar: ProgressBar? = null

    private var flagPermission: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_standard_web_view)

        initWeb()

        val intent = getIntent()
        val url = intent.getStringExtra("url")

        if (TextUtils.isEmpty(url)) {
            webView!!.loadUrl("file:///android_asset/gio_hybrid.html")
            //            webView.loadUrl("http://www.hybrid.com/");
        } else {
            webView!!.loadUrl(url)
        }

    }

    override  fun onResume() {
        super.onResume()
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initWeb() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            webView.setWebContentsDebuggingEnabled(true)
//        }

        webView!!.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {

                progressBar!!.progress = newProgress
                if (newProgress == 100) {
                    progressBar!!.visibility = View.GONE
                } else {
                    progressBar!!.visibility = View.VISIBLE
                }
                super.onProgressChanged(view, newProgress)
            }

            override fun onJsPrompt(view: WebView, url: String, message: String, defaultValue: String, result: JsPromptResult): Boolean {
                if (Build.VERSION.SDK_INT < 17) {

                    Thread(Runnable {
                        try {
                            val jsonObject = JSONObject(message)
                            val methodName = jsonObject.optString("methodName", null)
                            val jsonValue = jsonObject.optString("jsonValue", null)
                            jsCallManager!!.call(methodName, jsonValue)
                        } catch (e: Exception) {
                        }
                    }).start()
                }
                return super.onJsPrompt(view, url, message, defaultValue, result)
            }
        }


        webView!!.webViewClient = object : WebViewClient() {


            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()
                if (request.url == null) return false

                try {
                    if (url.startsWith("growing")) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        return true
                    }
                } catch (e: Exception) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }

                return false
            }

            //WebViewClient的方法 h5开始加载的回调
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                if (Build.VERSION.SDK_INT < 17) {
                    // 在 h5开始加载时动态给js注入Native对象和call方法,模拟addJavascriptInterface
                    //接口给js注入Native对象
                    //动态注入的好处就是不影响线上的h5数据,不影响ios使用
                    //在onPageStarted方法中注入是因为在h5的onload方法中有与本地交互的处理
                    //prompt()方法是js弹出的可输入的提示框
                    view.loadUrl("javascript:if(window.Native == undefined){window.Native={call:function(arg0,arg1){prompt('{\"methodName\":' + arg0 + ',\"jsonValue\":' + arg1 + '}')}}};")
                }
            }


        }

        webView!!.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            Toast.makeText(this@StandardWebView, "下载到：/sdcard/Download", Toast.LENGTH_SHORT).show()
            permissionCheck()
            if (flagPermission) {
                downLoadFile(url, contentDisposition, mimetype)

                // 使用
                val receiver = DownloadCompleteReceiver()
                val intentFilter = IntentFilter()
                intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                registerReceiver(receiver, intentFilter)

            }
        }


        //声明WebSettings子类
        val webSettings = webView!!.settings

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
            webView!!.addJavascriptInterface(JSCallManager(), "Native")
        } else {
            //4.2之前 addJavascriptInterface有安全泄漏风险
            //移除js中的searchBoxJavaBridge_对象,在Android 3.0以下，系统自己添加了一个叫
            //searchBoxJavaBridge_的Js接口，要解决这个安全问题，我们也需要把这个接口删除
            jsCallManager = JSCallManager()
            webView!!.removeJavascriptInterface("searchBoxJavaBridge_")
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun downLoadFile(url: String, contentDisposition: String, mimetype: String) {
        // 指定下载地址
        val request = DownloadManager.Request(Uri.parse(url))
        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
        request.allowScanningByMediaScanner()
        // 设置通知的显示类型，下载进行时和完成后显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        // 设置通知栏的标题，如果不设置，默认使用文件名
        //        request.setTitle("This is title");
        // 设置通知栏的描述
        //        request.setDescription("This is description");
        // 允许在计费流量下下载
        request.setAllowedOverMetered(false)
        // 允许该记录在下载管理界面可见
        request.setVisibleInDownloadsUi(true)
        // 允许漫游时下载
        request.setAllowedOverRoaming(true)
        // 允许下载的网路类型
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
        // 设置下载文件保存的路径和文件名
        //        String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(Date()) + "-" + URLUtil.guessFileName(url, contentDisposition, mimetype)
        Log.d("StandardWebView---->", fileName)

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        // 添加一个下载任务
        val downloadId = downloadManager.enqueue(request)
    }


    fun permissionCheck() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@StandardWebView, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder(this@StandardWebView).setTitle("请求权限啊")
                        .setMessage("需要下载文件，请赐予我力量")
                        .setPositiveButton("OK") { dialog, which -> ActivityCompat.requestPermissions(this@StandardWebView, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST) }
                        .create()
                        .show()
            } else {
                ActivityCompat.requestPermissions(this@StandardWebView, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST)
            }
        } else {
            flagPermission = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                flagPermission = (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    private inner class DownloadCompleteReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action) {
                    val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                    val type = downloadManager.getMimeTypeForDownloadedFile(downloadId)
                    if (TextUtils.isEmpty(type)) {
                        handleDownloadIntent(downloadManager, downloadId, "*/*")
                    } else if ("application/x-x509-ca-cert" == type) {
                        handleDownloadIntent(downloadManager, downloadId, type)
                    }

                }
            }
        }
    }


    fun handleDownloadIntent(downloadManager: DownloadManager, downloadId: Long, type: String) {
        val uri = downloadManager.getUriForDownloadedFile(downloadId)
        if (uri != null) {
            val handlerIntent = Intent(Intent.ACTION_VIEW)
            handlerIntent.setDataAndType(uri, type)
            startActivity(handlerIntent)
            finish()
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
