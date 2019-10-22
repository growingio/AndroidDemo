package com.growingio.giodemo.profile

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.growingio.android.sdk.utils.PermissionUtil
import com.growingio.giodemo.R
import kotlinx.android.synthetic.main.activity_get_debug_info.*
import kotlinx.android.synthetic.main.include_page_title.*


class GetDebugInfoActivity : AppCompatActivity() {
    private var imei: String = ""
    private var androidId: String = ""
    private var context: Context = this
    private var timer = object : CountDownTimer(60000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            //在计时器中轮询支付结果：每秒查询一次支付结果
            if (PermissionUtil.checkReadPhoneStatePermission()) {
                val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                imei = tm.deviceId
                androidId = Settings.System.getString(
                    context.contentResolver,
                    Settings.System.ANDROID_ID
                )
                tv_imei.text = imei
                tv_adroid.text = androidId
                cancel()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale
                        (
                        this@GetDebugInfoActivity,
                        Manifest.permission.READ_PHONE_STATE
                    )
                ) {
                    Toast.makeText(context, "GIO 需要此权限获得设备信息", Toast.LENGTH_LONG).show()
                }
                ActivityCompat.requestPermissions(
                    this@GetDebugInfoActivity,
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    1
                )
            }
            Log.e("haha", "start")
        }

        override fun onFinish() {

            Log.e("haha", "finish")

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_get_debug_info)
        head_title.text = "复制调试信息"
        back.setOnClickListener { finish() }

        timer.start()

        val cm: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        copy.setOnClickListener {
            cm.primaryClip = ClipData.newPlainText(null, "imei : $imei , Android ID : $androidId")
            Toast.makeText(context,"复制成功！",Toast.LENGTH_SHORT).show()
        }

    }

}