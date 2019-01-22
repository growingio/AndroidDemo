package com.growingio.giodemo

import android.app.Application
import com.growingio.android.sdk.collection.Configuration
import com.growingio.android.sdk.collection.GrowingIO

/**
 * classDesc: Application , 初始化 GrowingIO SDK
 */
class GIOApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        GrowingIO.startWithConfiguration(
            this, Configuration()
                .trackAllFragments()
                .setTestMode(true)
                .setDebugMode(true)
                .setChannel("XXX应用商店")
        )

    }
}
