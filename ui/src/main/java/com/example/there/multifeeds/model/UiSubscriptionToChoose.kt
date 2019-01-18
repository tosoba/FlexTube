package com.example.there.multifeeds.model

import android.databinding.ObservableField
import com.example.there.domain.model.Subscription
import com.example.there.multifeeds.util.view.ObservableSortedList

data class UiSubscriptionToChoose(
        val subscription: Subscription,
        val isChosen: ObservableField<Boolean> = ObservableField(false)
) {
    companion object {
        val observableSortedListCallback: ObservableSortedList.Callback<UiSubscriptionToChoose> = object : ObservableSortedList.Callback<UiSubscriptionToChoose> {
            override fun compare(
                    o1: UiSubscriptionToChoose,
                    o2: UiSubscriptionToChoose
            ): Int = o1.subscription.title.toLowerCase().compareTo(o2.subscription.title.toLowerCase())

            override fun areItemsTheSame(
                    item1: UiSubscriptionToChoose,
                    item2: UiSubscriptionToChoose
            ): Boolean = item1.subscription == item2.subscription

            override fun areContentsTheSame(
                    oldItem: UiSubscriptionToChoose,
                    newItem: UiSubscriptionToChoose
            ): Boolean = oldItem.subscription == newItem.subscription
        }
    }
}