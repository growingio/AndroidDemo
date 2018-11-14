package com.growingio.androiddemo.application

import android.app.Application
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
                    listMessage.add(eventJsonObject)
                }
        )

        meaningMap["t"] = "事件类型"
        meaningMap["s"] = "访问ID"
        meaningMap["tm"] = "事件时间"
        meaningMap["d"] = "包名"
        meaningMap["u"] = "访问用户id"
        meaningMap["uuid"] = "uuid"
        meaningMap["os"] = "系统"
        meaningMap["osv"] = "系统版本"
        meaningMap["p"] = "页面"
        meaningMap["ptm"] = "页面事件"
        meaningMap["e"] = "元素"
        meaningMap["r"] = "网络类型"
        meaningMap["var"] = "事件级变量"
        meaningMap["gesid"] = "全局请求编号"
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
        meaningMap["l"] = "设备语言"
        meaningMap["x"] = "元素XPath"
        meaningMap["imei"] = "IMEI"
        meaningMap["adrid"] = "安卓Id"
        meaningMap["o"] = "设备方向"
        meaningMap["tl"] = "页面标题"


        meaningMap["vst"] = "访问"
        meaningMap["page"] = "打开页面"
        meaningMap["imp"] = "浏览"
        meaningMap["clck"] = "点击"
        meaningMap["chng"] = "输入"
        meaningMap["reengage"] = "DeepLink唤醒"
        meaningMap["activate"] = "激活"
        meaningMap["cstm"] = "自定义事件"
        meaningMap["pvar"] = "页面级变量"
        meaningMap["ppl"] = "用户级变量"
        meaningMap["evar"] = "转化变量"
        meaningMap["vstr"] = "访问用户变量"

    }
}