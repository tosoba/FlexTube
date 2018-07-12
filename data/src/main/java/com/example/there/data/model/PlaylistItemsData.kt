package com.example.there.data.model

data class PlaylistItemsData(
        val playlistId: String,
        val videos: List<PlaylistItemData>,
        val nextPageToken: String? = null
) {
    companion object {
        fun empty(playlistId: String) = PlaylistItemsData(playlistId, emptyList(), null)
    }
}