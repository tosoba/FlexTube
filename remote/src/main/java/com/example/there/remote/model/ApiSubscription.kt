package com.example.there.remote.model

import java.util.*

data class SubscriptionsResponse(
        val nextPageToken: String?,
        val items: List<ApiSubscription>
)

data class ApiSubscription(
        val id: String,
        val snippet: SubscriptionSnippet
)

data class SubscriptionSnippet(
        val publishedAt: Date,
        val title: String,
        val description: String,
        val resourceId: SubscriptionResourceId,
        val thumbnails: SubscriptionThumbnails
)

data class SubscriptionResourceId(
        val channelId: String
)

data class SubscriptionThumbnails(
        val default: Thumbnail,
        val medium: Thumbnail,
        val high: Thumbnail
)

