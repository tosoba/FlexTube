package com.example.there.flextube.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class BaseBindingAdapter<I, B>(
        protected val items: List<I>,
        private val itemLayoutId: Int
) : RecyclerView.Adapter<BaseBindingViewHolder<B>>() where B : ViewDataBinding {

    private fun makeBinding(parent: ViewGroup): B {
        val inflater = LayoutInflater.from(parent.context)
        return DataBindingUtil.inflate(inflater, itemLayoutId, parent, false) as B
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<B> = BaseBindingViewHolder(makeBinding(parent))

    override fun getItemCount(): Int = items.size
}