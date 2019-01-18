package com.example.there.multifeeds.util.ext

import android.support.design.widget.Snackbar
import android.widget.TextView

fun Snackbar.setTextColor(color: Int) {
    (view.findViewById(android.support.design.R.id.snackbar_text) as? TextView)?.setTextColor(color)
}