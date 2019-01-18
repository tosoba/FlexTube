package com.example.there.data.mapper

import com.example.there.data.model.PlaylistItemData
import com.example.there.domain.model.PlaylistItem

object PlaylistItemMapper : TwoWayDataMapper<PlaylistItemData, PlaylistItem> {
    override fun toDomain(data: PlaylistItemData): PlaylistItem = PlaylistItem(
            channelId = data.channelId,
            thumbnailUrl = data.thumbnailUrl,
            title = data.title,
            description = data.description,
            publishedAt = data.publishedAt,
            videoId = data.videoId
    )

    override fun toData(domain: PlaylistItem): PlaylistItemData = PlaylistItemData(
            channelId = domain.channelId,
            thumbnailUrl = domain.thumbnailUrl,
            title = domain.title,
            description = domain.description,
            publishedAt = domain.publishedAt,
            videoId = domain.videoId
    )
}