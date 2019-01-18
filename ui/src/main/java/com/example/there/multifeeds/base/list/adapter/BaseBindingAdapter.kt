package com.example.there.multifeeds.base.list.adapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.there.multifeeds.base.list.viewholder.BaseBindingViewHolder
import com.example.there.multifeeds.util.ext.makeBinding

abstract class BaseBindingAdapter<I, B>(
        protected val items: List<I>,
        private val itemLayoutId: Int
) : RecyclerView.Adapter<BaseBindingViewHolder<B>>() where B : ViewDataBinding {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): BaseBindingViewHolder<B> = BaseBindingViewHolder(parent.makeBinding(itemLayoutId))

    override fun getItemCount(): Int = items.size
}

