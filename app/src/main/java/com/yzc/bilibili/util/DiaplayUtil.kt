package com.yzc.bilibili.util

import android.content.Context
import android.util.TypedValue

class DiaplayUtil {
}

fun Float.toPx(context: Context): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics).toInt()

fun Float.toDp(context: Context): Int = (this.toInt() / context.resources.displayMetrics.density).toInt()

fun Float.sp2px(context: Context): Int = (this.toInt() * context.resources.displayMetrics.scaledDensity + 0.5f).toInt()

fun Float.px2sp(context: Context): Int = (this / context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
