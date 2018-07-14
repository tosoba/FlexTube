package com.example.there.flextube.base

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.v7.util.SortedList

abstract class BaseObservableSortedListAdapter<I, B>(
        items: ObservableArrayList<I>,
        itemLayoutId: Int
) : BaseBindingAdapter<I, B>(items, itemLayoutId) where B : ViewDataBinding {

    init {
        items.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableArrayList<I>>() {
            override fun onChanged(sender: ObservableArrayList<I>?) = Unit

            override fun onItemRangeRemoved(sender: ObservableArrayList<I>?, positionStart: Int, itemCount: Int) {
                if (itemCount == sortedItems.size()) {
                    sortedItems.clear()
                }
            }

            override fun onItemRangeMoved(sender: ObservableArrayList<I>?, fromPosition: Int, toPosition: Int, itemCount: Int) = Unit

            override fun onItemRangeInserted(sender: ObservableArrayList<I>?, positionStart: Int, itemCount: Int) {
                val toAdd = sender?.subList(positionStart, positionStart + itemCount)
                sortedItems.addAll(toAdd)
            }

            override fun onItemRangeChanged(sender: ObservableArrayList<I>?, positionStart: Int, itemCount: Int) = Unit
        })
    }

    protected abstract val sortedItems: SortedList<I>

    override fun getItemCount(): Int = sortedItems.size()
}