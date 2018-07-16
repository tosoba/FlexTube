package com.example.there.cache.util

import com.example.there.cache.model.CachedPlaylistItem
import com.example.there.data.model.PlaylistItemData

fun PlaylistItemData.toCache(playlistId: String): CachedPlaylistItem =
        CachedPlaylistItem(channelId, title, description, thumbnailUrl, videoId, publishedAt, playlistId)

val CachedPlaylistItem.toData: PlaylistItemData
    get() = PlaylistItemData(channelId, title, description, thumbnailUrl, videoId, publishedAt)