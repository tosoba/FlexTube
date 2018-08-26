package com.example.there.flextube.base.list.adapter

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import com.example.there.flextube.util.ext.bindToItems

abstract class BaseObservableListAdapter<I, B>(
        items: ObservableList<I>,
        itemLayoutId: Int,
        itemsOffset: Int = 0
) : BaseBindingAdapter<I, B>(items, itemLayoutId) where B : ViewDataBinding {

    init {
        bindToItems(items, itemsOffset)
    }

    val observableItems: ObservableArrayList<I>
        get() = items as ObservableArrayList<I>
}

