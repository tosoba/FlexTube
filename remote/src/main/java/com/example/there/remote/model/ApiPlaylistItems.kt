package com.example.there.remote.model

import java.util.*

data class PlaylistItemsResponse(
        val items: List<ApiPlaylistItem>,
        val nextPageToken: String?
)

data class ApiPlaylistItem(
        val id: String,
        val snippet: PlaylistItemSnippet,
        val contentDetails: PlaylistItemContentDetails
)

data class PlaylistItemSnippet(
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: PlaylistItemThumbnails
)

data class PlaylistItemThumbnails(
        val default: Thumbnail,
        val medium: Thumbnail,
        val high: Thumbnail,
        val standard: Thumbnail
)

data class PlaylistItemContentDetails(
        val videoId: String,
        val videoPublishedAt: Date
)