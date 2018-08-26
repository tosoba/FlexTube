package com.example.there.flextube.base.list.adapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.there.flextube.R
import com.example.there.flextube.base.list.viewholder.BaseBindingViewHolder
import com.example.there.flextube.base.list.viewholder.BaseViewHolder
import com.example.there.flextube.databinding.LoadingItemBinding
import com.example.there.flextube.util.ext.makeBinding

abstract class BaseBindingLoadingAdapter<I, B>(
        protected val items: List<I>,
        private val itemLayoutId: Int,
        private val loadingItemLayoutId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() where B : ViewDataBinding {

    var loadingInProgress: Boolean = false

    override fun getItemViewType(
            position: Int
    ): Int = if (position == items.size) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        VIEW_TYPE_LOADING -> BaseBindingViewHolder<LoadingItemBinding>(parent!!.makeBinding(loadingItemLayoutId))
        VIEW_TYPE_ITEM -> BaseBindingViewHolder<B>(parent!!.makeBinding(itemLayoutId))
        else -> throw IllegalStateException("${javaClass.name}: Unknown viewType: $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is BaseViewHolder) {
            val progressBar = holder.itemView.findViewById<ProgressBar>(R.id.loading_item_progress_bar)
            if (loadingInProgress) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = items.size + 1

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_ITEM = 1
    }
}