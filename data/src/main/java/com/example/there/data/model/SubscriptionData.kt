package com.example.there.data.model

import java.util.*

data class SubscriptionData(
        val id: String,
        val publishedAt: Date,
        val title: String,
        val description: String,
        val channelId: String,
        val thumbnailUrl: String
)