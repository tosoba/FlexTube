package com.example.there.remote.model

import java.util.*

data class VideosSearchResponse(
        val nextPageToken: String?,
        val items: List<VideoSearchItem>
)

data class VideoSearchItem(
        val id: VideoSearchItemId,
        val snippet: VideoSearchItemSnippet
)

data class VideoSearchItemId(
        val videoId: String
)

data class VideoSearchItemSnippet(
        val publishedAt: Date,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: VideoSearchItemThumbnails,
        val channelTitle: String
)

data class VideoSearchItemThumbnails(
        val default: Thumbnail,
        val medium: Thumbnail,
        val high: Thumbnail
)