package com.example.there.remote.model

import java.util.*

data class HomeItemsResponse(
        val nextPageToken: String?,
        val items: List<HomeItem>
)

data class HomeItem(
        val id: String,
        val snippet: HomeItemSnippet,
        val contentDetails: HomeItemContentDetails
)

data class HomeItemSnippet(
        val publishedAt: Date,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: HomeItemThumbnails,
        val channelTitle: String,
        val type: String
)

data class HomeItemThumbnails(
        val default: Thumbnail,
        val medium: Thumbnail,
        val high: Thumbnail,
        val maxres: Thumbnail
)

data class HomeItemContentDetails(
        val upload: HomeItemContentDetailsUpload?
)

data class HomeItemContentDetailsUpload(
        val videoId: String
)