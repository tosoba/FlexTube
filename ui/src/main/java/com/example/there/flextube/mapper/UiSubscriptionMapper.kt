package com.example.there.flextube.mapper

import com.example.there.domain.model.Subscription
import com.example.there.flextube.model.UiSubscription

object UiSubscriptionMapper: UiMapper<Subscription, UiSubscription> {
    override fun toDomain(ui: UiSubscription): Subscription = Subscription(
            id = ui.id,
            channelId = ui.channelId,
            publishedAt = ui.publishedAt,
            description = ui.description,
            thumbnailUrl = ui.thumbnailUrl,
            title = ui.title
    )

    override fun toUi(domain: Subscription): UiSubscription = UiSubscription(
            id = domain.id,
            channelId = domain.channelId,
            publishedAt = domain.publishedAt,
            description = domain.description,
            thumbnailUrl = domain.thumbnailUrl,
            title = domain.title
    )
}