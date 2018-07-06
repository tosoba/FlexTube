package com.example.there.data.repo.store.subscription.base

import com.example.there.data.model.SubscriptionData
import io.reactivex.Completable
import io.reactivex.Observable

interface IYoutubeDataStore {
    fun getUserSubscriptions(accessToken: String): Observable<List<SubscriptionData>>
    fun saveUserSubscriptions(subs: List<SubscriptionData>): Completable
}