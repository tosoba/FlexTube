package com.example.there.flextube.list

import android.databinding.ObservableList
import com.example.there.flextube.base.list.adapter.BaseObservableListAdapter
import com.example.there.flextube.base.list.viewholder.BaseBindingViewHolder
import com.example.there.flextube.databinding.SubscriptionItemBinding
import com.example.there.flextube.model.UiSubscription

class SubscriptionsAdapter(
        items: ObservableList<UiSubscription>,
        itemLayoutId: Int
) : BaseObservableListAdapter<UiSubscription, SubscriptionItemBinding>(items, itemLayoutId) {

    override fun onBindViewHolder(holder: BaseBindingViewHolder<SubscriptionItemBinding>?, position: Int) {
        holder?.binding?.subscription = items[position]
    }
}