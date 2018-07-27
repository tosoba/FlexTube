package com.example.there.data.repo

import com.example.there.data.mapper.GroupMapper
import com.example.there.data.mapper.PlaylistItemMapper
import com.example.there.data.mapper.SubscriptionMapper
import com.example.there.data.mapper.VideoCategoryMapper
import com.example.there.data.model.ChannelPlaylistIdData
import com.example.there.data.model.PlaylistData
import com.example.there.data.model.PlaylistItemData
import com.example.there.data.repo.store.IYoutubeCache
import com.example.there.data.repo.store.IYoutubeRemote
import com.example.there.domain.model.Group
import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.model.Subscription
import com.example.there.domain.model.VideoCategory
import com.example.there.domain.repo.IMainRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class MainRepository @Inject constructor(
        private val youtubeRemoteDataStore: IYoutubeRemote,
        private val youtubeCachedDataStore: IYoutubeCache
) : IMainRepository {

    private fun concatSavedAndRemotePlaylistItems(
            remote: Single<Pair<List<PlaylistItemData>, String?>>,
            savedVideos: List<PlaylistItemData>
    ): Single<Pair<ArrayList<PlaylistItemData>, String?>> = remote.map { (remoteVideos, nextPageToken) ->
        ArrayList<PlaylistItemData>(remoteVideos.size + savedVideos.size).apply {
            addAll(savedVideos)
            addAll(remoteVideos)
        } to nextPageToken
    }

    private fun handleHomeItems(
            shouldReturnAll: Boolean,
            savedVideos: List<PlaylistItemData>,
            nextPageToken: String?,
            remote: Single<Pair<List<PlaylistItemData>, String?>>
    ): Single<out Pair<List<PlaylistItemData>, String?>> = if (savedVideos.isEmpty() || nextPageToken != null) {
        if (shouldReturnAll) concatSavedAndRemotePlaylistItems(remote, savedVideos)
        else remote
    } else if (shouldReturnAll) {
        Single.just(savedVideos to nextPageToken)
    } else {
        Single.error { IllegalStateException("No more home items to retrieve.") }
    }

    override fun getGeneralHomeItems(
            accessToken: String,
            shouldReturnAll: Boolean
    ): Single<List<PlaylistItem>> = youtubeCachedDataStore.getSavedHomeItems(IYoutubeCache.CATEGORY_GENERAL)
            .flatMap { (savedVideos, nextPageToken) ->
                handleHomeItems(shouldReturnAll, savedVideos, nextPageToken, retrieveAndSaveGeneralHomeItems(accessToken, nextPageToken))
            }
            .map { (videos, _) -> videos.map(PlaylistItemMapper::toDomain) }

    private fun retrieveAndSaveGeneralHomeItems(
            accessToken: String,
            pageToken: String?
    ): Single<Pair<List<PlaylistItemData>, String?>> = youtubeRemoteDataStore.getGeneralHomeItems(accessToken, pageToken)
            .doOnSuccess { (videos, nextPageToken) ->
                youtubeCachedDataStore.saveHomeItems(IYoutubeCache.CATEGORY_GENERAL, videos, nextPageToken)
            }

    override fun getHomeItemsByCategory(
            categoryId: String,
            shouldReturnAll: Boolean
    ): Single<List<PlaylistItem>> = youtubeCachedDataStore.getSavedHomeItems(categoryId)
            .flatMap { (savedVideos, nextPageToken) ->
                handleHomeItems(shouldReturnAll, savedVideos, nextPageToken, retrieveAndSaveHomeItemsFromCategory(categoryId, nextPageToken))
            }
            .map { (videos, _) -> videos.map(PlaylistItemMapper::toDomain) }

    private fun retrieveAndSaveHomeItemsFromCategory(
            categoryId: String,
            pageToken: String?
    ): Single<Pair<List<PlaylistItemData>, String?>> = youtubeRemoteDataStore.getHomeItemsByCategory(categoryId, pageToken)
            .doOnSuccess { (videos, nextPageToken) ->
                youtubeCachedDataStore.saveHomeItems(categoryId, videos, nextPageToken)
            }

    override fun loadRelatedVideos(
            videoId: String,
            shouldReturnAll: Boolean
    ): Single<List<PlaylistItem>> = youtubeCachedDataStore.getSavedRelatedVideos(videoId)
            .flatMap { (savedVideos, nextPageToken) ->
                handleRelatedVideos(shouldReturnAll, savedVideos, nextPageToken, retrieveAndSaveRelatedVideos(videoId, nextPageToken))
            }
            .map { (videos, _) -> videos.map(PlaylistItemMapper::toDomain) }

    private fun retrieveAndSaveRelatedVideos(
            videoId: String,
            pageToken: String?
    ): Single<Pair<List<PlaylistItemData>, String?>> = youtubeRemoteDataStore.getRelatedVideos(videoId, pageToken)
            .doOnSuccess { (videos, nextPageToken) ->
                youtubeCachedDataStore.saveRelatedVideos(videoId, videos, nextPageToken)
            }

    private fun handleRelatedVideos(
            shouldReturnAll: Boolean,
            savedVideos: List<PlaylistItemData>,
            nextPageToken: String?,
            remote: Single<Pair<List<PlaylistItemData>, String?>>
    ): Single<out Pair<List<PlaylistItemData>, String?>> = if (savedVideos.isEmpty() || nextPageToken != null) {
        if (shouldReturnAll) concatSavedAndRemotePlaylistItems(remote, savedVideos)
        else remote
    } else if (shouldReturnAll) {
        Single.just(savedVideos to nextPageToken)
    } else {
        Single.error { IllegalStateException("No more related videos to retrieve.") }
    }

    override fun getSubscriptions(
            accessToken: String,
            accountName: String
    ): Observable<List<Subscription>> = youtubeCachedDataStore.saveUser(accountName)
            .andThen(youtubeRemoteDataStore.getUserSubscriptions(accessToken)
                    .flatMap {
                        youtubeCachedDataStore.saveUserSubscriptions(it, accountName)
                                .andThen(Observable.just(it))
                    }
                    .map { it.map(SubscriptionMapper::toDomain) })

    override fun getSavedSubsriptions(
            accountName: String
    ): Flowable<List<Subscription>> = youtubeCachedDataStore.getUserSubscriptions(accountName)
            .map { it.map(SubscriptionMapper::toDomain) }

    override fun updateSavedSubscriptions(
            subs: List<Subscription>, accountName: String
    ): Completable = youtubeCachedDataStore.updateSavedSubscriptions(subs.map(SubscriptionMapper::toData), accountName)

    override fun getGroups(
            accountName: String
    ): Flowable<List<Group>> = youtubeCachedDataStore.getGroupsForAccount(accountName)
            .map { it.map(GroupMapper::toDomain) }

    override fun getSubscriptionsFromGroup(
            accountName: String,
            groupName: String
    ): Single<List<Subscription>> = youtubeCachedDataStore.getSubscriptionsFromGroup(accountName, groupName)
            .map { it.map(SubscriptionMapper::toDomain) }

    override fun getVideoCategories(): Single<List<VideoCategory>> = youtubeRemoteDataStore.getVideoCategories()
            .map { it.map(VideoCategoryMapper::toDomain) }

    private fun getChannelPlaylistIds(
            channelIds: List<String>
    ): Observable<ChannelPlaylistIdData> = Observable.just(channelIds)
            .flatMapIterable { it }
            .buffer(50)
            .flatMap { youtubeRemoteDataStore.getChannelsPlaylistIds(it).toObservable() }
            .flatMapIterable { it }

    override fun loadVideos(
            channelIds: List<String>
    ): Observable<List<PlaylistItem>> = getChannelPlaylistIds(channelIds)
            .flatMap { cp ->
                youtubeCachedDataStore.savePlaylist(cp.playlistId, cp.channelId)
                        .andThen(loadSaveAndReturnRemoteVideos(cp.playlistId))
            }

    override fun loadMoreVideos(
            channelIds: List<String>
    ): Completable = getPlaylistData(channelIds)
            .toObservable()
            .flatMapIterable { it.toList() }
            .flatMapCompletable { loadAndSaveRemoteVideos(it.id, it.nextPageToken) }

    private fun loadAndSaveRemoteVideos(
            playlistId: String,
            pageToken: String? = null
    ): Completable = youtubeRemoteDataStore.getPlaylistItems(playlistId, pageToken)
            .flatMapCompletable { (videos, nextPageToken) ->
                youtubeCachedDataStore.saveRetrievedVideos(playlistId, videos, nextPageToken)
            }

    private fun loadSaveAndReturnRemoteVideos(
            playlistId: String,
            pageToken: String? = null
    ): Observable<List<PlaylistItem>> = youtubeRemoteDataStore.getPlaylistItems(playlistId, pageToken)
            .toObservable()
            .flatMap { (videos, nextPageToken) ->
                youtubeCachedDataStore.saveRetrievedVideos(playlistId, videos, nextPageToken).andThen(Observable.just(videos))
            }
            .map { it.map(PlaylistItemMapper::toDomain) }

    private fun getPlaylistData(
            channelIds: List<String>
    ): Single<List<PlaylistData>> = Single.zip(channelIds.map { youtubeCachedDataStore.getPlaylistByChannelId(it) }) {
        it.map { it as PlaylistData }
    }

    @Suppress("UNCHECKED_CAST")
    override fun getSavedVideosWithUpdates(
            channelIds: List<String>
    ): Flowable<List<PlaylistItem>> = Flowable.zip(channelIds.map { youtubeCachedDataStore.getSavedVideosFlowable(it) }) {
        (it.map { it as List<PlaylistItemData> }).toList().flatten()
    }.map {
        it.map(PlaylistItemMapper::toDomain)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getSavedVideos(
            channelIds: List<String>
    ): Single<List<PlaylistItem>> = Single.zip(channelIds.map { youtubeCachedDataStore.getSavedVideos(it) }) {
        (it.map { it as List<PlaylistItemData> }).toList().flatten()
    }.map {
        it.map(PlaylistItemMapper::toDomain)
    }

    override fun getGroup(
            groupName: String,
            accountName: String
    ): Single<Group> = youtubeCachedDataStore.getGroup(groupName, accountName).map(GroupMapper::toDomain)

    override fun insertGroupWithSubscriptions(
            groupName: String,
            accountName: String,
            subscriptionIds: List<String>
    ): Completable = youtubeCachedDataStore.insertGroupWithSubscriptions(groupName, accountName, subscriptionIds)
}
