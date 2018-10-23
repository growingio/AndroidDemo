package com.growingio.androiddemo

import android.app.Application
import com.growingio.android.sdk.collection.Configuration
import com.growingio.android.sdk.collection.GrowingIO

/**
 * classDesc:
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        GrowingIO.startWithConfiguration(this, Configuration()
                .trackAllFragments()
                .setChannel("XXX应用商店")
        )

    }
}