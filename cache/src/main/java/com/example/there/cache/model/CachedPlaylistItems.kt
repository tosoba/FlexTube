package com.example.there.cache.model

import com.example.there.data.model.PlaylistItemData
import java.util.*

data class CachedPlaylistItems(
        val videos: HashSet<PlaylistItemData>,
        var nextPageToken: String? = null
)