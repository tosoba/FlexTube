package com.example.there.flextube.addgroup

import android.databinding.ObservableField
import com.example.there.flextube.model.UiSubscriptionToChoose
import com.example.there.flextube.util.view.ObservableSortedList

data class AddGroupViewState(
        val subscriptions: ObservableSortedList<UiSubscriptionToChoose> = ObservableSortedList(
                UiSubscriptionToChoose::class.java,
                UiSubscriptionToChoose.observableSortedListCallback
        ),
        val noSubscriptions: ObservableField<Boolean> = ObservableField(false)
)