package com.example.there.remote.mapper

import com.example.there.data.model.PlaylistItemData
import com.example.there.remote.model.ApiPlaylistItem

object ApiPlaylistItemMapper: ApiMapper<ApiPlaylistItem, PlaylistItemData> {
    override fun toData(api: ApiPlaylistItem): PlaylistItemData = PlaylistItemData(
            id = api.id,
            channelId = api.snippet.channelId,
            thumbnailUrl = api.snippet.thumbnails.high.url,
            title = api.snippet.title,
            description = api.snippet.description,
            publishedAt = api.contentDetails.videoPublishedAt,
            videoId = api.contentDetails.videoId
    )
}