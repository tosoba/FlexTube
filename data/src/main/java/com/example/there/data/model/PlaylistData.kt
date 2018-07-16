package com.example.there.data.model

data class PlaylistData(
        val id: String,
        val channelId: String,
        var nextPageToken: String? = null
)