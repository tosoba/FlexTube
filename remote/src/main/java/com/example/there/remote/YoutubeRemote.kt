package com.example.there.remote

import com.example.there.data.model.SubscriptionData
import com.example.there.data.repo.store.subscription.base.IYoutubeRemote
import com.example.there.remote.mapper.ApiSubscriptionMapper
import io.reactivex.Single
import javax.inject.Inject

class YoutubeRemote @Inject constructor(private val service: YoutubeService) : IYoutubeRemote {
    override fun getSubscriptions(
            accessToken: String,
            pageToken: String?
    ): Single<Pair<List<SubscriptionData>, String?>> = service.getUserSubscriptions(
            authorization = "Bearer $accessToken",
            pageToken = pageToken
    ).map { Pair(it.items.map(ApiSubscriptionMapper::map), it.nextPageToken) }
}