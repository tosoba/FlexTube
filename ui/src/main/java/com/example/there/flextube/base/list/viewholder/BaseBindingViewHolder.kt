package com.example.there.flextube.base.list.viewholder

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

class BaseBindingViewHolder<B>(val binding: B) : RecyclerView.ViewHolder(binding.root) where B : ViewDataBinding