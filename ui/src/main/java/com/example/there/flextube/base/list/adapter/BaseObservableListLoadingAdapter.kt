package com.example.there.flextube.base.list.adapter

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import com.example.there.flextube.util.ext.bindToItems

abstract class BaseObservableListLoadingAdapter<I, B>(
        items: ObservableList<I>,
        itemLayoutId: Int,
        loadingItemLayoutId: Int
) : BaseBindingLoadingAdapter<I, B>(items, itemLayoutId, loadingItemLayoutId) where B : ViewDataBinding {

    init {
        bindToItems(items)
    }
}