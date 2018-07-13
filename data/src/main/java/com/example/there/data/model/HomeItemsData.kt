package com.example.there.data.model

data class HomeItemsData(
        val videos: List<PlaylistItemData>,
        val nextPageToken: String? = null
) {
    companion object {
        fun empty(): HomeItemsData = HomeItemsData(emptyList())
    }
}