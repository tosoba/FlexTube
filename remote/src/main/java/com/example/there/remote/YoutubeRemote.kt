package com.example.there.remote

import com.example.there.data.model.ChannelPlaylistIdData
import com.example.there.data.model.PlaylistItemData
import com.example.there.data.model.SubscriptionData
import com.example.there.data.repo.store.IYoutubeRemote
import com.example.there.remote.mapper.ApiActivityMapper
import com.example.there.remote.mapper.ApiChannelPlaylistIdMapper
import com.example.there.remote.mapper.ApiPlaylistItemMapper
import com.example.there.remote.mapper.ApiSubscriptionMapper
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

    override fun getActivities(
            accessToken: String,
            pageToken: String?
    ): Single<List<PlaylistItemData>> = service.getActivities(authorization = "Bearer $accessToken")
            .map { it.items.filter { it.contentDetails.upload != null }.map(ApiActivityMapper::toData) }

    private fun getSubscriptions(
            accessToken: String,
            pageToken: String?
    ): Single<Pair<List<SubscriptionData>, String?>> = service.getUserSubscriptions(
            authorization = "Bearer $accessToken",
            pageToken = pageToken
    ).map { Pair(it.items.map(ApiSubscriptionMapper::toData), it.nextPageToken) }
}