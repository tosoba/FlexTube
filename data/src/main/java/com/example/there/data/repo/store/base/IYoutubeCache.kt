package com.example.there.data.repo.store.base

import com.example.there.data.model.SubscriptionData
import io.reactivex.Completable
import io.reactivex.Single

interface IYoutubeCache {
    fun getSubscriptionsForAccount(accountName: String): Single<List<SubscriptionData>>
    fun getSubscriptionsFromGroup(groupName: String): Single<List<SubscriptionData>>
    fun saveSubscriptions(subs: List<SubscriptionData>, accountName: String): Completable
    fun updateSavedSubscriptions(subs: List<SubscriptionData>, accountName: String): Completable

    fun saveAccount(accountName: String): Completable
}