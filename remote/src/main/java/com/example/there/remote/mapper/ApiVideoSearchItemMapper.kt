package com.example.there.remote.mapper

import com.example.there.data.model.PlaylistItemData
import com.example.there.remote.model.ApiVideoSearchItem

object ApiVideoSearchItemMapper : ApiMapper<ApiVideoSearchItem, PlaylistItemData> {
    override fun toData(api: ApiVideoSearchItem): PlaylistItemData = PlaylistItemData(
            channelId = api.snippet.channelId,
            title = api.snippet.title,
            videoId = api.id.videoId,
            description = api.snippet.description,
            publishedAt = api.snippet.publishedAt,
            thumbnailUrl = api.snippet.thumbnails.medium.url
    )
}