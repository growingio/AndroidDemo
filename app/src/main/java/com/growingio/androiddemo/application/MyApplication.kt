package com.growingio.androiddemo.application

import android.app.Application
import com.growingio.android.sdk.collection.Configuration
import com.growingio.android.sdk.collection.GrowingIO

/**
 * classDesc:
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //GrowingIO 初始化配置
        GrowingIO.startWithConfiguration(this, Configuration()
                .trackAllFragments()
                .setChannel("XXX应用商店")
        )

    }
}