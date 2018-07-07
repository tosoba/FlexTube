package com.example.there.data.model

import java.util.*

data class PlaylistItemData(
        val id: String,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnailUrl: String,
        val videoId: String,
        val publishedAt: Date
)