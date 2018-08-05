package com.example.there.flextube.subfeed

import android.databinding.ObservableArrayList
import com.example.there.domain.model.Subscription
import com.example.there.flextube.base.list.adapter.BaseObservableListAdapter
import com.example.there.flextube.base.list.viewholder.BaseBindingViewHolder
import com.example.there.flextube.databinding.SubscriptionItemBinding

class SubFeedSubscriptionsAdapter(
        items: ObservableArrayList<Subscription>,
        itemLayoutId: Int
) : BaseObservableListAdapter<Subscription, SubscriptionItemBinding>(items, itemLayoutId) {

    override fun onBindViewHolder(holder: BaseBindingViewHolder<SubscriptionItemBinding>?, position: Int) {
        holder?.binding?.subscription = items[position]
    }
}