package com.example.there.remote.mapper

import com.example.there.data.model.ChannelPlaylistIdData
import com.example.there.remote.model.ChannelPlaylistIdItem


object ApiChannelPlaylistIdMapper: ApiMapper<ChannelPlaylistIdItem, ChannelPlaylistIdData> {
    override fun toData(api: ChannelPlaylistIdItem): ChannelPlaylistIdData = ChannelPlaylistIdData(
            channelId = api.id,
            playlistId = api.contentDetails.relatedPlaylists.uploads
    )
}