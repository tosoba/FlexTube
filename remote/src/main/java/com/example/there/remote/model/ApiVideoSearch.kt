package com.example.there.remote.model

import java.util.*

data class RelatedVideosSearchResponse(
        val nextPageToken: String?,
        val items: List<RelatedVideoSearchItem>
)

data class RelatedVideoSearchItem(
        val id: RelatedVideoSearchItemId,
        val snippet: RelatedVideoSearchItemSnippet
)

data class RelatedVideoSearchItemId(
        val videoId: String
)

data class RelatedVideoSearchItemSnippet(
        val publishedAt: Date,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: RelatedVideoSearchItemThumbnails,
        val channelTitle: String
)

data class RelatedVideoSearchItemThumbnails(
        val default: Thumbnail,
        val medium: Thumbnail,
        val high: Thumbnail
)