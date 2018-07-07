package com.example.there.data.repo.store.impl

import com.example.there.data.model.PlaylistItemData
import com.example.there.data.model.SubscriptionData
import com.example.there.data.repo.store.base.IYoutubeCache
import com.example.there.data.repo.store.base.IYoutubeDataStore
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class YoutubeCachedDataStore @Inject constructor(private val cache: IYoutubeCache): IYoutubeDataStore {
    override fun saveUser(accountName: String): Completable = cache.saveAccount(accountName)

    override fun getUserSubscriptions(accessToken: String, accountName: String): Observable<List<SubscriptionData>> =
            cache.getSubscriptionsForAccount(accountName).toObservable()

    override fun saveUserSubscriptions(subs: List<SubscriptionData>, accountName: String): Completable =
            cache.saveSubscriptions(subs, accountName)

    override fun getVideos(channelIds: List<String>): Observable<List<PlaylistItemData>> = throw UnsupportedOperationException()

    companion object {
        const val NAME = "CACHED_DATA_STORE"
    }
}