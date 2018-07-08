package com.example.there.remote.model

import java.util.*

data class ActivityResponse(
        val nextPageToken: String?,
        val items: List<ActivityItem>
)

data class ActivityItem(
    val id: String,
    val snippet: ActivitySnippet,
    val contentDetails: ActivityContentDetails
)

data class ActivitySnippet(
        val publishedAt: Date,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: ActivityThumbnails,
        val channelTitle: String,
        val type: String
)

data class ActivityThumbnails(
        val default: Thumbnail,
        val medium: Thumbnail,
        val high: Thumbnail,
        val maxres: Thumbnail
)

data class ActivityContentDetails(
        val upload: ActivityContentDetailsUpload?
)

data class ActivityContentDetailsUpload(
        val videoId: String
)