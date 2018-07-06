package com.example.there.data.repo.store.subscription.base

import com.example.there.data.model.SubscriptionData
import io.reactivex.Single

interface IYoutubeRemote {
    fun getSubscriptions(accessToken: String, pageToken: String? = null): Single<Pair<List<SubscriptionData>, String?>>
}