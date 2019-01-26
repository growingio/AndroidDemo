package com.growingio.giodemo

import android.content.Intent

data class Product(
    val name: String, val price: Int,
    val categoryImg: Int, val sellImg: Int, val desc: String
)


val gioCup =
    Product(
        "GIO 马克杯",
        39,
        R.drawable.hot5,
        R.drawable.pd_cup,
        "办公用品马克杯"
    )
val gioShirt =
    Product(
        "GIO 帽衫",
        99,
        R.drawable.hot6,
        R.drawable.hot6_big,
        "多一件不多的帽衫"
    )
val theHandBookOfGrowthHacker =
    Product(
        "增长黑客手册",
        0,
        R.drawable.hot2,
        R.drawable.hot2_big,
        "不同于市场上已有的案例汇总书籍，这是第一本系统介绍增长黑客能力的手册，覆盖基础知识、进阶能力和团队搭建。"
    )
val theHandBookOfDataOperation =
    Product(
        "数据运营手册",
        0,
        R.drawable.hot3,
        R.drawable.hot3_big,
        "《数据运营手册：方法、工具、案例》系统总结了 GrowingIO 创业以来在数据运营方面的经验，是第一本系统介绍数据运营能力的电子书。"
    )
val theHandBookOfPMAnalytics = Product(
    "产品经理数据分析手册",
    0,
    R.drawable.hot4,
    R.drawable.hot4_big,
    "《产品经理数据分析手册》 总结了 GrowingIO 创业以来的产品经理数据分析精华，包括产品经理数据分析 4 大案例、5 大能力、6 本书籍、7 种工具、8 大方法。"
)

val goods =
    arrayOf(gioCup, gioShirt, theHandBookOfGrowthHacker, theHandBookOfDataOperation, theHandBookOfPMAnalytics)

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