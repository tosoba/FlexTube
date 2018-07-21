package com.example.there.flextube.addgroup

import android.databinding.ObservableArrayList
import com.example.there.flextube.model.UiSubscriptionToChoose

data class AddGroupViewState(val subscriptions: ObservableArrayList<UiSubscriptionToChoose> = ObservableArrayList())