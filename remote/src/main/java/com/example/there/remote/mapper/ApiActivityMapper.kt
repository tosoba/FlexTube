package com.example.there.remote.mapper

import com.example.there.data.model.PlaylistItemData
import com.example.there.remote.model.HomeItem

object ApiActivityMapper : ApiMapper<HomeItem, PlaylistItemData> {
    override fun toData(api: HomeItem): PlaylistItemData = PlaylistItemData(
            videoId = api.contentDetails.upload!!.videoId,
            channelId = api.snippet.channelId,
            description = api.snippet.description,
            publishedAt = api.snippet.publishedAt,
            thumbnailUrl = api.snippet.thumbnails.medium.url,
            title = api.snippet.title
    )
}