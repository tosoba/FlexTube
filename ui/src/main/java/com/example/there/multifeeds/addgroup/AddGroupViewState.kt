package com.example.there.multifeeds.addgroup

import android.databinding.ObservableField
import com.example.there.multifeeds.model.UiSubscriptionToChoose
import com.example.there.multifeeds.util.view.ObservableSortedList

data class AddGroupViewState(
        val subscriptions: ObservableSortedList<UiSubscriptionToChoose> = ObservableSortedList(
                UiSubscriptionToChoose::class.java,
                UiSubscriptionToChoose.observableSortedListCallback
        ),
        val noSubscriptions: ObservableField<Boolean> = ObservableField(false)
)