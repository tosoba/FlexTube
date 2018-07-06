package com.example.there.data.mapper

import com.example.there.data.model.SubscriptionData
import com.example.there.domain.model.Subscription

object SubscriptionMapper{
     fun map(from: SubscriptionData): Subscription = Subscription(
            id = from.id,
            channelId = from.channelId,
            title = from.title,
            description = from.description,
            thumbnailUrl = from.thumbnailUrl,
            publishedAt = from.publishedAt
    )

     fun mapBack(from: Subscription): SubscriptionData = SubscriptionData(
            id = from.id,
            channelId = from.channelId,
            title = from.title,
            description = from.description,
            thumbnailUrl = from.thumbnailUrl,
            publishedAt = from.publishedAt
    )

}