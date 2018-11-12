package com.growingio.androiddemo.application

import android.app.Application
import android.util.Log
import com.growingio.android.sdk.collection.Configuration
import com.growingio.android.sdk.collection.GrowingIO
import org.json.JSONObject

/**
 * classDesc: Application
 */
class MyApplication : Application() {

    var listMessage: MutableList<JSONObject> = ArrayList()
    var meaningMap: MutableMap<String, String> = HashMap()
    override fun onCreate() {
        super.onCreate()


        //GrowingIO 初始化配置
        GrowingIO.startWithConfiguration(this, Configuration()
                .trackAllFragments()
                .setTestMode(true)
                .setDebugMode(true)
                .setChannel("XXX应用商店")
                .addRealTimeMessageCallBack { eventType, eventJsonObject ->
                    Log.d("haha", "$eventType  ->  $eventJsonObject")
                    listMessage.add(eventJsonObject)
                }
        )

        meaningMap["t"] = "事件类型"
        meaningMap["s"] = "session"
        meaningMap["tm"] = "事件时间"
        meaningMap["d"] = "包名"
        meaningMap["u"] = "匿名用户id"
        meaningMap["uuid"] = "uuid"
        meaningMap["os"] = "系统"
        meaningMap["osv"] = "系统版本"
        meaningMap["p"] = "页面"
        meaningMap["ptm"] = "页面事件"
        meaningMap["e"] = "元素"
        meaningMap["r"] = "网络类型"
        meaningMap["var"] = "事件级变量"
        meaningMap["gesid"] = "全局请求id"
        meaningMap["esid"] = "请求id"
        meaningMap["db"] = "设备品牌"
        meaningMap["ph"] = "设备类型"
        meaningMap["dm"] = "设备型号"
        meaningMap["av"] = "SDK版本"
        meaningMap["cv"] = "客户端版本"
        meaningMap["sn"] = "应用名称"
        meaningMap["sw"] = "屏幕宽度"
        meaningMap["sh"] = "屏幕高度"
        meaningMap["ch"] = "渠道来源"
        meaningMap["lng"] = "经度"
        meaningMap["lat"] = "纬度"
        meaningMap["v"] = "URLScheme"

    }
}