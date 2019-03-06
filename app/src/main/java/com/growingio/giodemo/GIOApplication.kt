package com.growingio.giodemo

import android.app.Application
import android.util.Log
import com.growingio.android.sdk.collection.Configuration
import com.growingio.android.sdk.collection.GrowingIO
import com.growingio.android.sdk.gtouch.GrowingTouch
import com.growingio.android.sdk.gtouch.config.GTouchConfig
import com.growingio.android.sdk.gtouch.listener.EventPopupListener

/**
 * classDesc: Application , 初始化 GrowingIO SDK
 */

const val TAG: String = "GIOApplication"

class GIOApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        GrowingIO.startWithConfiguration(
            this, Configuration()
                .trackAllFragments()
                .setTestMode(true)
                .setDebugMode(true)
                .setChannel(BuildConfig.CHANNEL)
        )
        GrowingIO.getInstance().userId = "GIOXiaoYing"

        GrowingTouch.startWithConfig(
            this, GTouchConfig()
                .setDebugEnable(true)
                .setEventPopupEnable(true)
                .setEventPopupListener(object : EventPopupListener {
                    override fun onLoadFailed(
                        eventId: String?,
                        eventType: String?,
                        errorCode: Int,
                        description: String?
                    ) {
                        Log.e(
                            TAG,
                            "onLoadFailed: eventId = $eventId, eventType = $eventType， description = $description"
                        )
                    }

                    override fun onCancel(eventId: String?, eventType: String?) {
                        Log.e(TAG, "onCancel: eventId = $eventId, eventType = $eventType")
                    }

                    override fun onLoadSuccess(eventId: String?, eventType: String?) {
                        Log.e(TAG, "onLoadSuccess: eventId = $eventId, eventType = $eventType")
                    }

                    override fun onClicked(eventId: String?, eventType: String?, openUrl: String?): Boolean {
                        Log.e(TAG, "onClicked: eventId = $eventId, eventType = $eventType openUrl = $openUrl")
                        return false
                    }

                    override fun onTimeout(eventId: String?, eventType: String?) {
                        Log.e(TAG, "onTimeout: eventId = $eventId, eventType = $eventType")
                    }

                })
        )

        //会员等级
        GrowingIO.getInstance().setPeopleVariable("vipLevel", 1)

    }
}
