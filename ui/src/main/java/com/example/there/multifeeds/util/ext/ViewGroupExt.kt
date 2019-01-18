package com.example.there.multifeeds.util.ext

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup

fun <B> ViewGroup.makeBinding(itemLayoutId: Int): B where B : ViewDataBinding {
    val inflater = LayoutInflater.from(context)
    return DataBindingUtil.inflate(inflater, itemLayoutId, this, false) as B
}