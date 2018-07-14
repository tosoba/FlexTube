package com.example.there.remote.model

import java.util.*

data class SearchVideosByCategoryResponse(
        val nextPageToken: String?,
        val items: List<ApiVideoSearchItem>
)

data class ApiVideoSearchItem(
        val id: ApiVideoSearchItemId,
        val snippet: ApiVideoSearchItemSnippet,
        val channelTitle: String
)

data class ApiVideoSearchItemId(
        val videoId: String
)

data class ApiVideoSearchItemSnippet(
        val publishedAt: Date,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: ApiVideoSearchItemThumbnails
)

data class ApiVideoSearchItemThumbnails(
        val default: Thumbnail,
        val medium: Thumbnail,
        val high: Thumbnail
)