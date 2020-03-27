package com.growingio.giodemo.cart

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.growingio.android.sdk.collection.GrowingIO
import com.growingio.giodemo.*
import kotlinx.android.synthetic.main.fragment_shopping_cart.view.*
import org.json.JSONObject
import java.io.File
import java.math.BigDecimal

class ShoppingCartFragment : Fragment() {
    private lateinit var adapter: MyGoodsAdapter
    private var mIsAttach = false
    private var mIsVisible = false
    private var mIsCreated = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(userVisibleHint){
            mIsVisible = true
            lazyLoad()
        }else{
            mIsVisible = false
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (!mIsAttach) {
            mIsAttach = true
            lazyLoad()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_cart, container, false)

        adapter = MyGoodsAdapter(activity!!, listProduct!!, null, null)
        view.rc_cart.adapter = adapter

        view.rc_cart.layoutManager = LinearLayoutManager(activity)

        view.btn_sbmt_order.isEnabled = listProduct!!.size > 0

        view.btn_sbmt_order.setOnClickListener {
            startActivity(Intent(activity, SubmtOrderActivity::class.java))

            for (product in listProduct!!) {
                GrowingIO.getInstance().track(
                    "checkOut",
                    JSONObject()
                        .put(GioProductId, product.id)
                        .put(GioProductName, product.name)
                        .put(GioBuyQuantity, 1)
                        .put(GioPayment, product.price)
                )
            }

        }

        view.clear_all.isEnabled = true
        view.clear_all.setOnClickListener{
            clearCache(context!!)
        }

        if (!mIsCreated) {
            mIsCreated = true
            lazyLoad()
        }
        return view
    }

    override fun onDetach() {
        super.onDetach()
        mIsAttach = false
    }

    private fun lazyLoad() {
        if (mIsVisible && mIsAttach && mIsCreated) {
            productInTheCart(context!!)
            adapter.notifyDataSetChanged()
        }
    }


    fun File.suicide() {
        if (isFile) delete()
        if (isDirectory) listFiles().forEach { it.suicide() }
    }

    private fun getCacheSize(context: Context): Long {
        val cacheSize = context.cacheDir.length()
        val fileSize = context.filesDir.length()
        return cacheSize + fileSize
    }

    fun clearCache(context: Context) {
        val cacheSize = getCacheSize(context)
        if (cacheSize > 0) {
            context.alert("清除所有缓存？", "清除", "取消") {
                context.cacheDir.suicide()
                context.filesDir.suicide()
                Toast.makeText(context,"清除数据${cacheSize.formatMemorySize()}成功",Toast.LENGTH_SHORT).show()
            }
        } else Toast.makeText(context,"无须清理",Toast.LENGTH_SHORT).show()
    }

    fun Context.alert(message: String, confirmText: String, cancelText: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setNegativeButton(cancelText) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(confirmText) { dialog, _ ->
                dialog.dismiss()
                onConfirm()
            }.show()
    }

    fun Long.formatMemorySize(): String {
        val kiloByte = this / 1024.toDouble()

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            return kiloByte.toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "K"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            return megaByte.toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "M"
        }

        val teraBytes = megaByte / 1024
        if (teraBytes < 1) {
            return megaByte.toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "G"
        }

        return teraBytes.toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "T"
    }
}