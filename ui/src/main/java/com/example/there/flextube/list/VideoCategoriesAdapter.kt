package com.example.there.flextube.list

import android.databinding.ObservableArrayList
import com.example.there.flextube.base.BaseBindingViewHolder
import com.example.there.flextube.base.BaseObservableListAdapter
import com.example.there.flextube.databinding.VideoCategoryItemBinding
import com.example.there.flextube.model.UiVideoCategory

class VideoCategoriesAdapter(
        items: ObservableArrayList<UiVideoCategory>,
        itemLayoutId: Int
): BaseObservableListAdapter<UiVideoCategory, VideoCategoryItemBinding>(items, itemLayoutId) {

    override fun onBindViewHolder(holder: BaseBindingViewHolder<VideoCategoryItemBinding>?, position: Int) {
        val category = items[position]
        holder?.binding?.category = category
    }
}