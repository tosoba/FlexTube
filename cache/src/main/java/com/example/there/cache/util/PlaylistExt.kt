package com.example.there.cache.util

import com.example.there.cache.model.CachedPlaylist
import com.example.there.data.model.PlaylistData

val PlaylistData.toCache: CachedPlaylist
    get() = CachedPlaylist(id, channelId, nextPageToken)

val CachedPlaylist.toData: PlaylistData
    get() = PlaylistData(id, channelId, nextPageToken)