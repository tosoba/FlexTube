package com.example.there.remote.model

data class ChannelsPlaylistIdResponse(
        val items: List<ChannelPlaylistIdItem>
)

data class ChannelPlaylistIdItem(
        val id: String,
        val contentDetails: ChannelPlaylistIdContentDetails
)

data class ChannelPlaylistIdContentDetails(
        val relatedPlaylists: ChannelRelatedPlaylists
)

data class ChannelRelatedPlaylists(
        val uploads: String
)