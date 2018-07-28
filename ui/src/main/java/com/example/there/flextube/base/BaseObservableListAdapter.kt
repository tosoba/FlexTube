package com.example.there.flextube.base

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding

abstract class BaseObservableListAdapter<I, B>(
        items: ObservableList<I>,
        itemLayoutId: Int
) : BaseBindingAdapter<I, B>(items, itemLayoutId) where B : ViewDataBinding {

    init {
        items.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<I>>() {
            override fun onChanged(sender: ObservableList<I>?) = notifyDataSetChanged()

            override fun onItemRangeRemoved(sender: ObservableList<I>?, positionStart: Int, itemCount: Int) =
                    notifyItemRangeRemoved(positionStart, itemCount)

            override fun onItemRangeMoved(sender: ObservableList<I>?, fromPosition: Int, toPosition: Int, itemCount: Int) =
                    notifyItemMoved(fromPosition, toPosition)

            override fun onItemRangeInserted(sender: ObservableList<I>?, positionStart: Int, itemCount: Int) =
                    notifyItemRangeInserted(positionStart, itemCount)

            override fun onItemRangeChanged(sender: ObservableList<I>?, positionStart: Int, itemCount: Int) =
                    notifyItemRangeChanged(positionStart, itemCount)
        })
    }

    val observableItems: ObservableArrayList<I>
        get() = items as ObservableArrayList<I>
}