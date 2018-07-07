package com.example.there.data.mapper

import com.example.there.data.model.SubscriptionData
import com.example.there.domain.model.Subscription

object SubscriptionMapper: DataMapper<SubscriptionData, Subscription> {
     override fun toDomain(data: SubscriptionData): Subscription = Subscription(
            id = data.id,
            channelId = data.channelId,
            title = data.title,
            description = data.description,
            thumbnailUrl = data.thumbnailUrl,
            publishedAt = data.publishedAt
    )

     override fun toData(domain: Subscription): SubscriptionData = SubscriptionData(
            id = domain.id,
            channelId = domain.channelId,
            title = domain.title,
            description = domain.description,
            thumbnailUrl = domain.thumbnailUrl,
            publishedAt = domain.publishedAt
    )

}