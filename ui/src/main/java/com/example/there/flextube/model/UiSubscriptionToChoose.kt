package com.example.there.flextube.model

import android.databinding.ObservableField
import com.example.there.domain.model.Subscription

data class UiSubscriptionToChoose(
        val subscription: Subscription,
        val isChosen: ObservableField<Boolean> = ObservableField(false)
)