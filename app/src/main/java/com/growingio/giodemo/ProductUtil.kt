package com.growingio.giodemo

import android.content.Context
import android.content.Intent
import com.growingio.giodemo.cart.defaultPrefs
import com.growingio.giodemo.cart.get

data class Product(
    val id: String,
    val name: String,
    val price: Int,
    val categoryImg: Int,
    val sellImg: Int,
    val desc: String,
    val status: String,
    val orderNumber: String
)

//打点事件变量名
const val GioProductId = "productId_var"
const val GioProductName = "productName_var"
const val GioAdPosition = "floor_var"
const val GioSearchKeyWord = "searchWord_var"
const val GioBuyQuantity = "buyQuantity_var"
const val GioOrderId = "orderId_var"
const val GioOrderAmount = "orderAmount"
const val GioPayMethod = "paymentMethod_var"
const val GioPayment = "payAmount_var"
const val GioShareChannel = "sharechannel"

//转化事件流量位标示
const val EVAR_BANNER = "banner"
const val EVAR_LIMITED = "限时秒杀"
const val EVAR_SUGGESTED = "GIO推荐"
const val EVAR_PRODUCTS_SUGGESTED = "商品详情页推荐"


val gioCup =
    Product(
        "001",
        "GIO 马克杯",
        39,
        R.drawable.hot5,
        R.drawable.pd_cup,
        "办公用品马克杯",
        "待付款",
        Math.random().toString().substring(3, 13)
    )
val gioShirt =
    Product(
        "002",
        "GIO 帽衫",
        99,
        R.drawable.hot6,
        R.drawable.hot6_big,
        "多一件不多的帽衫",
        "配送中",
        Math.random().toString().substring(3, 13)
    )
val theHandBookOfGrowthHacker =
    Product(
        "003",
        "增长黑客手册",
        0,
        R.drawable.hot2,
        R.drawable.hot2_big,
        "不同于市场上已有的案例汇总书籍，这是第一本系统介绍增长黑客能力的手册，覆盖基础知识、进阶能力和团队搭建。",
        "已完成",
        Math.random().toString().substring(3, 13)
    )
val theHandBookOfDataOperation =
    Product(
        "004",
        "数据运营手册",
        0,
        R.drawable.hot3,
        R.drawable.hot3_big,
        "《数据运营手册：方法、工具、案例》系统总结了 GrowingIO 创业以来在数据运营方面的经验，是第一本系统介绍数据运营能力的电子书。",
        "已完成",
        Math.random().toString().substring(3, 13)
    )
val theHandBookOfPMAnalytics = Product(
    "005",
    "产品经理数据分析手册",
    0,
    R.drawable.hot4,
    R.drawable.hot4_big,
    "《产品经理数据分析手册》 总结了 GrowingIO 创业以来的产品经理数据分析精华，包括产品经理数据分析 4 大案例、5 大能力、6 本书籍、7 种工具、8 大方法。",
    "配送中",
    Math.random().toString().substring(3, 13)
)

val goods =
    arrayOf(gioCup, gioShirt, theHandBookOfGrowthHacker, theHandBookOfDataOperation, theHandBookOfPMAnalytics)

var listProduct: MutableList<Product>? = ArrayList()

fun productInTheCart(context: Context): MutableList<Product>? {
    listProduct!!.clear()
    if (defaultPrefs(context)[gioCup.name]!!) {
        listProduct!!.add(gioCup)
    }
    if (defaultPrefs(context)[gioShirt.name]!!) {
        listProduct!!.add(gioShirt)
    }
    if (defaultPrefs(context)[theHandBookOfDataOperation.name]!!) {
        listProduct!!.add(theHandBookOfDataOperation)
    }
    if (defaultPrefs(context)[theHandBookOfGrowthHacker.name]!!) {
        listProduct!!.add(theHandBookOfGrowthHacker)
    }
    if (defaultPrefs(context)[theHandBookOfPMAnalytics.name]!!) {
        listProduct!!.add(theHandBookOfPMAnalytics)
    }

    return listProduct
}

fun syncProduct(intent: Intent): Product? {
    return when (intent.getStringExtra("product")) {
        gioCup.name -> gioCup

        gioShirt.name -> gioShirt

        theHandBookOfGrowthHacker.name -> theHandBookOfGrowthHacker

        theHandBookOfDataOperation.name -> theHandBookOfDataOperation

        theHandBookOfPMAnalytics.name -> theHandBookOfPMAnalytics

        else -> null
    }
}