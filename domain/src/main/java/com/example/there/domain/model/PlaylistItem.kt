package com.example.there.domain.model

import java.util.*

data class PlaylistItem(
        val id: String,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnailUrl: String,
        val videoId: String,
        val publishedAt: Date
)