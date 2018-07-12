package com.example.there.flextube.util.ext

import android.content.Context
import android.util.TypedValue


fun Context.toPx(dp: Int): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()

val Context.screenOrientation: Int
    get() = resources.configuration.orientation

val Context.screenHeight: Int
    get() = resources.configuration.screenHeightDp

val Context.screenWidth: Int
    get() = resources.configuration.screenWidthDp