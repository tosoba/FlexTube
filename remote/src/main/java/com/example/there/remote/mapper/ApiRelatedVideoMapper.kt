package com.example.there.remote.mapper

import com.example.there.data.model.PlaylistItemData
import com.example.there.remote.model.VideoSearchItem

object ApiRelatedVideoMapper: ApiMapper<VideoSearchItem, PlaylistItemData> {
    override fun toData(api: VideoSearchItem): PlaylistItemData = PlaylistItemData(
            channelId = api.snippet.channelId,
            title = api.snippet.title,
            description = api.snippet.description,
            publishedAt = api.snippet.publishedAt,
            videoId = api.id.videoId,
            thumbnailUrl = api.snippet.thumbnails.medium.url
    )
}