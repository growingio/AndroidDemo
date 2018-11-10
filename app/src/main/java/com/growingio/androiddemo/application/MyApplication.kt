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

    public var listMessage: MutableList<JSONObject> = ArrayList()

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

    }
}