package com.example.there.data.model

data class SavedPlaylistItemsData(
        val videos: List<PlaylistItemData>,
        val nextPageToken: String? = null
) {
    companion object {
        fun empty(): SavedPlaylistItemsData = SavedPlaylistItemsData(emptyList())
    }
}