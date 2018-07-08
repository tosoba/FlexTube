package com.example.there.cache

import com.example.there.cache.db.FlexTubeDb
import com.example.there.cache.model.CachedAccount
import com.example.there.cache.util.toCache
import com.example.there.cache.util.toData
import com.example.there.data.model.SubscriptionData
import com.example.there.data.repo.store.base.IYoutubeCache
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class YoutubeCache @Inject constructor(db: FlexTubeDb) : IYoutubeCache {
    private val accountDao = db.accountDao()

    private val subscriptionDao = db.subscriptionDao()
    override fun getSubscriptionsForAccount(
            accountName: String
    ): Single<List<SubscriptionData>> = subscriptionDao.getAllByAccountName(accountName)
            .map { it.map { it.toData } }

    override fun getSubscriptionsFromGroup(
            groupName: String
    ): Single<List<SubscriptionData>> = subscriptionDao.getAllByGroupName(groupName)
            .map { it.map { it.toData } }

    override fun saveSubscriptions(subs: List<SubscriptionData>, accountName: String): Completable = Completable.fromAction {
        subscriptionDao.insertMany(*subs.map { it.toCache(accountName) }.toTypedArray())
    }

    override fun updateSavedSubscriptions(subs: List<SubscriptionData>, accountName: String): Completable =
            subscriptionDao.getAllByAccountName(accountName)
                    .flatMap { savedSubs ->
                        Single.just(subscriptionDao.deleteMany(*savedSubs.filter { savedSub ->
                            !subs.map { it.id }.contains(savedSub.id)
                        }.toTypedArray()))
                    }.toCompletable()

    override fun saveAccount(accountName: String): Completable = Completable.fromAction {
        accountDao.insert(CachedAccount(accountName))
    }
}