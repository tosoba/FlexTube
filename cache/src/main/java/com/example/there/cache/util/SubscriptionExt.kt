package com.example.there.cache.util

import com.example.there.cache.model.CachedSubscription
import com.example.there.data.model.SubscriptionData

val CachedSubscription.toData: SubscriptionData
    get() = SubscriptionData(id, publishedAt, title, description, channelId, thumbnailUrl)

fun SubscriptionData.toCache(accountName: String): CachedSubscription =
        CachedSubscription(id, publishedAt, title, description, channelId, thumbnailUrl, accountName)