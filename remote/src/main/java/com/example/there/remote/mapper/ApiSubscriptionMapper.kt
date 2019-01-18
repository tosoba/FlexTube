package com.example.there.remote.mapper

import com.example.there.data.model.SubscriptionData
import com.example.there.remote.model.ApiSubscription

object ApiSubscriptionMapper : ApiMapper<ApiSubscription, SubscriptionData> {
    override fun toData(api: ApiSubscription): SubscriptionData = SubscriptionData(
            id = api.id,
            publishedAt = api.snippet.publishedAt,
            title = api.snippet.title,
            description = api.snippet.description,
            channelId = api.snippet.resourceId.channelId,
            thumbnailUrl = api.snippet.thumbnails.medium.url
    )
}