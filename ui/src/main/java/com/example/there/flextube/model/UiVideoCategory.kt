package com.example.there.flextube.model

import android.databinding.ObservableField

data class UiVideoCategory(
        val id: String,
        val title: String,
        val imageResourceId: Int,
        val isSelected: ObservableField<Boolean> = ObservableField(false)
)