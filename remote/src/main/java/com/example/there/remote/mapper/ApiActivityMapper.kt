package com.example.there.remote.mapper

import com.example.there.data.model.PlaylistItemData
import com.example.there.remote.model.ActivityItem

object ApiActivityMapper : ApiMapper<ActivityItem, PlaylistItemData> {
    override fun toData(api: ActivityItem): PlaylistItemData = PlaylistItemData(
            videoId = api.contentDetails.upload!!.videoId,
            channelId = api.snippet.channelId,
            description = api.snippet.description,
            publishedAt = api.snippet.publishedAt,
            thumbnailUrl = api.snippet.thumbnails.medium.url,
            title = api.snippet.title
    )
}