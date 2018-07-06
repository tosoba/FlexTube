package com.example.there.remote.mapper

import com.example.there.data.model.SubscriptionData
import com.example.there.remote.model.ApiSubscription

object ApiSubscriptionMapper {
     fun map(from: ApiSubscription): SubscriptionData = SubscriptionData(
            id = from.id,
            publishedAt = from.snippet.publishedAt,
            title = from.snippet.title,
            description = from.snippet.description,
            channelId = from.snippet.resourceId.channelId,
            thumbnailUrl = from.snippet.thumbnails.medium.url
    )
}