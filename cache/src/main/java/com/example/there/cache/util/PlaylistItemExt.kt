package com.example.there.cache.util

import com.example.there.cache.model.CachedPlaylistItem
import com.example.there.data.model.PlaylistItemData

fun PlaylistItemData.toCache(playlistId: String): CachedPlaylistItem = CachedPlaylistItem(
        channelId = channelId,
        title = title,
        description = description,
        thumbnailUrl = thumbnailUrl,
        videoId = videoId,
        publishedAt = publishedAt,
        playlistId = playlistId
)

val CachedPlaylistItem.toData: PlaylistItemData
    get() = PlaylistItemData(
            channelId = channelId,
            title = title,
            description = description,
            thumbnailUrl = thumbnailUrl,
            videoId = videoId,
            publishedAt = publishedAt
    )