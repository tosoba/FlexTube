package com.example.there.cache

import com.example.there.cache.db.MultiFeedsDb
import com.example.there.cache.model.*
import com.example.there.cache.util.toCache
import com.example.there.cache.util.toData
import com.example.there.data.model.*
import com.example.there.data.repo.store.IYoutubeCache
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YoutubeCache @Inject constructor(db: MultiFeedsDb) : IYoutubeCache {
    private val accountDao = db.accountDao()

    private val subscriptionDao = db.subscriptionDao()

    private val playlistItemDao = db.playlistItemDao()

    private val playlistDao = db.playlistDao()

    private val groupDao = db.groupDao()

    private val subscriptionGroupJoinDao = db.subscriptionGroupsJoinDao()

    override fun saveUser(accountName: String): Completable = Completable.fromAction {
        accountDao.insert(CachedAccount(accountName))
    }

    override fun getUserSubscriptions(
            accountName: String
    ): Flowable<List<SubscriptionData>> = subscriptionDao.getAllFlowableByAccountName(accountName)
            .map { it.map { it.toData } }

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

    private val savedHomeItems: ConcurrentHashMap<String, CachedPlaylistItems> = ConcurrentHashMap()

    override fun saveHomeItems(categoryId: String, videos: List<PlaylistItemData>, nextPageToken: String?) {
        val saved = savedHomeItems[categoryId]
        if (saved != null) {
            saved.videos.addAll(videos)
            saved.nextPageToken = nextPageToken
        } else {
            savedHomeItems[categoryId] = CachedPlaylistItems(hashSetOf(*videos.toTypedArray()), nextPageToken)
        }
    }

    override fun getSavedHomeItems(categoryId: String): Single<SavedPlaylistItemsData> {
        val saved = savedHomeItems[categoryId]
        return if (saved == null) Single.just(SavedPlaylistItemsData.empty())
        else Single.just(SavedPlaylistItemsData(saved.videos.toList(), saved.nextPageToken))
    }

    private val savedRelatedVideos: ConcurrentHashMap<String, CachedPlaylistItems> = ConcurrentHashMap()

    override fun saveRelatedVideos(videoId: String, videos: List<PlaylistItemData>, nextPageToken: String?) {
        val saved = savedRelatedVideos[videoId]
        if (saved != null) {
            saved.videos.addAll(videos)
            saved.nextPageToken = nextPageToken
        } else {
            savedRelatedVideos[videoId] = CachedPlaylistItems(hashSetOf(*videos.toTypedArray()), nextPageToken)
        }
    }

    override fun getSavedRelatedVideos(videoId: String): Single<SavedPlaylistItemsData> {
        val saved = savedRelatedVideos[videoId]
        return if (saved == null) Single.just(SavedPlaylistItemsData.empty())
        else Single.just(SavedPlaylistItemsData(saved.videos.toList(), saved.nextPageToken))
    }

    private val savedFoundVideos: ConcurrentHashMap<String, CachedPlaylistItems> = ConcurrentHashMap()

    override fun saveFoundVideos(query: String, videos: List<PlaylistItemData>, nextPageToken: String?) {
        val saved = savedFoundVideos[query]
        if (saved != null) {
            saved.videos.addAll(videos)
            saved.nextPageToken = nextPageToken
        } else {
            savedFoundVideos[query] = CachedPlaylistItems(hashSetOf(*videos.toTypedArray()), nextPageToken)
        }
    }

    override fun getSavedFoundVideos(query: String): Single<SavedPlaylistItemsData> {
        val saved = savedFoundVideos[query]
        return if (saved == null) Single.just(SavedPlaylistItemsData.empty())
        else Single.just(SavedPlaylistItemsData(saved.videos.toList(), saved.nextPageToken))
    }

    override fun getPlaylistByChannelId(channelId: String): Single<PlaylistData> = playlistDao.getByChannelId(channelId).map { it.toData }

    override fun getPlaylistById(id: String): Single<PlaylistData> = playlistDao.getById(id).map { it.toData }

    override fun savePlaylist(playlistId: String, channelId: String): Completable = Completable.fromAction {
        playlistDao.insert(CachedPlaylist(playlistId, channelId))
    }

    override fun updatePlaylistNextPageToken(id: String, nextPageToken: String?): Completable = Completable.fromAction {
        playlistDao.updateNextPageToken(id, nextPageToken)
    }

    override fun saveRetrievedVideos(
            playlistId: String,
            videos: List<PlaylistItemData>,
            nextPageToken: String?
    ): Completable = Completable.fromAction {
        playlistDao.updateNextPageToken(playlistId, nextPageToken)
        playlistItemDao.insertMany(*videos.map { it.toCache(playlistId) }.toTypedArray())
    }

    override fun getSavedVideosFlowable(
            channelId: String
    ): Flowable<List<PlaylistItemData>> = playlistItemDao.getAllFlowableByChannelId(channelId)
            .map { it.map { it.toData } }

    override fun getSavedVideos(
            channelId: String
    ): Single<List<PlaylistItemData>> = playlistItemDao.getAllByChannelId(channelId)
            .map { it.map { it.toData } }

    override fun getGroupsForAccount(
            accountName: String
    ): Flowable<List<GroupData>> = groupDao.getAllByAccountName(accountName)
            .map { it.map { it.toData } }

    override fun getSubscriptionsFromGroup(
            accountName: String,
            groupName: String
    ): Flowable<List<SubscriptionData>> = subscriptionDao.getAllByGroup(accountName, groupName)
            .map { it.map { it.toData } }

    override fun getGroup(
            groupName: String,
            accountName: String
    ): Single<GroupData> = groupDao.get(groupName, accountName).map { it.toData }

    override fun deleteGroup(
            groupName: String,
            accountName: String
    ): Completable = Completable.fromAction {
        groupDao.delete(CachedGroup(groupName, accountName))
    }

    override fun insertGroupWithSubscriptions(
            groupName: String,
            accountName: String,
            subscriptionIds: List<String>
    ): Completable = Completable.fromAction {
        groupDao.insert(CachedGroup(groupName, accountName))
        subscriptionGroupJoinDao.insertMany(*subscriptionIds.map { SubscriptionGroupJoin(it, groupName, accountName) }.toTypedArray())
    }

    override fun addSubscriptionsToGroup(
            groupName: String,
            accountName: String,
            subscriptionIds: List<String>
    ): Completable = Completable.fromAction {
        subscriptionGroupJoinDao.insertMany(*subscriptionIds.map { SubscriptionGroupJoin(it, groupName, accountName) }.toTypedArray())
    }

    override fun getSubscriptionsNotAddedToGroup(
            accountName: String,
            groupName: String
    ): Flowable<List<SubscriptionData>> = subscriptionDao.getAllNotFromGroup(accountName, groupName)
            .map { it.map { it.toData } }
}
