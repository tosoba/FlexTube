package com.example.there.domain.model

import java.util.*

data class Subscription(
        val id: String,
        val publishedAt: Date,
        val title: String,
        val description: String,
        val channelId: String,
        val thumbnailUrl: String
)