package com.example.there.remote

import com.example.there.data.model.ChannelPlaylistIdData
import com.example.there.data.model.PlaylistItemData
import com.example.there.data.model.SubscriptionData
import com.example.there.data.model.VideoCategoryData
import com.example.there.data.repo.store.IYoutubeRemote
import com.example.there.remote.mapper.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class YoutubeRemote @Inject constructor(private val service: YoutubeService) : IYoutubeRemote {
    override fun getUserSubscriptions(accessToken: String): Observable<List<SubscriptionData>> {
        val pageTokenSubject = BehaviorSubject.createDefault("")
        return pageTokenSubject.concatMap { token ->
            getSubscriptions(
                    pageToken = if (token == "") null else token,
                    accessToken = accessToken
            ).toObservable()
        }.doOnNext { (_, token) ->
            if (token != null) pageTokenSubject.onNext(token)
            else pageTokenSubject.onComplete()
        }.map { (subs, _) -> subs }
    }

    override fun getChannelsPlaylistIds(
            channelIds: List<String>
    ): Single<List<ChannelPlaylistIdData>> = service.getChannelsPlaylistId(ids = channelIds.joinToString())
            .map { it.items.map(ApiChannelPlaylistIdMapper::toData) }

    override fun getPlaylistItems(
            channelId: String,
            pageToken: String?
    ): Single<Pair<List<PlaylistItemData>, String?>> = service.getPlaylistItems(id = channelId, pageToken = pageToken)
            .map { it.items.map(ApiPlaylistItemMapper::toData) to it.nextPageToken }

    override fun getGeneralHomeItems(
            accessToken: String,
            pageToken: String?
    ): Single<Pair<List<PlaylistItemData>, String?>> = service.getHomeItems(authorization = "Bearer $accessToken", pageToken = pageToken)
            .map {
                it.items.filter { it.contentDetails.upload != null }
                        .map(ApiActivityMapper::toData) to it.nextPageToken
            }

    override fun getHomeItemsByCategory(
            categoryId: String,
            pageToken: String?
    ): Single<Pair<List<PlaylistItemData>, String?>> = service.getVideosByCategory(categoryId, pageToken)
            .map { it.items.map(ApiVideoSearchItemMapper::toData) to it.nextPageToken }

    private fun getSubscriptions(
            accessToken: String,
            pageToken: String?
    ): Single<Pair<List<SubscriptionData>, String?>> = service.getUserSubscriptions(
            authorization = "Bearer $accessToken",
            pageToken = pageToken
    ).map { it.items.map(ApiSubscriptionMapper::toData) to it.nextPageToken }

    override fun getVideoCategories(): Single<List<VideoCategoryData>> = service.getVideoCategories()
            .map { it.items.map(ApiVideoCategoryMapper::toData) }

    override fun getRelatedVideos(
            videoId: String,
            pageToken: String?
    ): Single<Pair<List<PlaylistItemData>, String?>> = service.getRelatedVideos(videoId, pageToken)
            .map { it.items.map(ApiRelatedVideoMapper::toData) to it.nextPageToken }

    override fun searchForVideos(
            query: String,
            pageToken: String?
    ): Single<Pair<List<PlaylistItemData>, String?>> = service.searchForVideos(query, pageToken)
            .map { it.items.map(ApiRelatedVideoMapper::toData) to it.nextPageToken }
}
