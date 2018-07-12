package com.example.there.flextube.base

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding

abstract class BaseObservableListAdapter<I, B>(
        items: ObservableArrayList<I>,
        itemLayoutId: Int
) : BaseBindingAdapter<I, B>(items, itemLayoutId) where B : ViewDataBinding {

    init {
        items.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableArrayList<I>>() {
            override fun onChanged(sender: ObservableArrayList<I>?) = notifyDataSetChanged()

            override fun onItemRangeRemoved(sender: ObservableArrayList<I>?, positionStart: Int, itemCount: Int) =
                    notifyItemRangeRemoved(positionStart, itemCount)

            override fun onItemRangeMoved(sender: ObservableArrayList<I>?, fromPosition: Int, toPosition: Int, itemCount: Int) =
                    notifyItemMoved(fromPosition, toPosition)

            override fun onItemRangeInserted(sender: ObservableArrayList<I>?, positionStart: Int, itemCount: Int) =
                    notifyItemRangeInserted(positionStart, itemCount)

            override fun onItemRangeChanged(sender: ObservableArrayList<I>?, positionStart: Int, itemCount: Int) =
                    notifyItemRangeChanged(positionStart, itemCount)
        })
    }
}