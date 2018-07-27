package com.example.there.remote.mapper

import com.example.there.data.model.PlaylistItemData
import com.example.there.remote.model.RelatedVideoSearchItem

object ApiRelatedVideoMapper: ApiMapper<RelatedVideoSearchItem, PlaylistItemData> {
    override fun toData(api: RelatedVideoSearchItem): PlaylistItemData = PlaylistItemData(
            channelId = api.snippet.channelId,
            title = api.snippet.title,
            description = api.snippet.description,
            publishedAt = api.snippet.publishedAt,
            videoId = api.id.videoId,
            thumbnailUrl = api.snippet.thumbnails.medium.url
    )
}