package com.example.there.cache

import com.example.there.cache.db.FlexTubeDb
import com.example.there.cache.model.CachedAccount
import com.example.there.cache.model.CachedPlaylistItems
import com.example.there.cache.util.toCache
import com.example.there.cache.util.toData
import com.example.there.data.model.PlaylistItemData
import com.example.there.data.model.PlaylistItemsData
import com.example.there.data.model.SubscriptionData
import com.example.there.data.repo.store.IYoutubeCache
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YoutubeCache @Inject constructor(db: FlexTubeDb) : IYoutubeCache {
    private val accountDao = db.accountDao()

    private val subscriptionDao = db.subscriptionDao()

    override fun saveUser(accountName: String): Completable = Completable.fromAction {
        accountDao.insert(CachedAccount(accountName))
    }

    override fun getUserSubscriptions(
            accountName: String
    ): Observable<List<SubscriptionData>> = subscriptionDao.getAllByAccountName(accountName)
            .map { it.map { it.toData } }.toObservable()

    override fun saveUserSubscriptions(
            subs: List<SubscriptionData>,
            accountName: String
    ): Completable = Completable.fromAction {
        subscriptionDao.insertMany(*subs.map { it.toCache(accountName) }.toTypedArray())
    }

    override fun updateSavedSubscriptions(
            subs: List<SubscriptionData>,
            accountName: String
    ): Completable = subscriptionDao.getAllByAccountName(accountName)
            .flatMap { savedSubs ->
                Single.just(subscriptionDao.deleteMany(*savedSubs.filter { savedSub ->
                    !subs.map { it.id }.contains(savedSub.id)
                }.toTypedArray()))
            }.toCompletable()

    override fun getSubscriptionsFromGroup(
            groupName: String
    ): Single<List<SubscriptionData>> = subscriptionDao.getAllByGroupName(groupName)
            .map { it.map { it.toData } }

    private val savedPlaylistItems: ConcurrentHashMap<String, CachedPlaylistItems> = ConcurrentHashMap()

    override fun saveRetrievedVideos(
            playlistId: String,
            videos: List<PlaylistItemData>,
            nextPageToken: String?
    ) {
        val saved = savedPlaylistItems[playlistId]
        if (saved != null) {
            saved.videos.addAll(videos)
            saved.nextPageToken = nextPageToken
        } else {
            savedPlaylistItems[playlistId] = CachedPlaylistItems(hashSetOf(*videos.toTypedArray()), nextPageToken)
        }
    }

    override fun getSavedVideos(
            playlistId: String
    ): Single<PlaylistItemsData> {
        val saved = savedPlaylistItems[playlistId]
        return if (saved == null) Single.just(PlaylistItemsData.empty(playlistId))
        else Single.just(PlaylistItemsData(playlistId, saved.videos.toList(), saved.nextPageToken))
    }
}
