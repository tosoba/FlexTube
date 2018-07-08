package com.example.there.remote

import com.example.there.data.model.ChannelPlaylistIdData
import com.example.there.data.model.PlaylistItemData
import com.example.there.data.model.SubscriptionData
import com.example.there.data.repo.store.base.IYoutubeRemote
import com.example.there.remote.mapper.ApiActivityMapper
import com.example.there.remote.mapper.ApiChannelPlaylistIdMapper
import com.example.there.remote.mapper.ApiPlaylistItemMapper
import com.example.there.remote.mapper.ApiSubscriptionMapper
import io.reactivex.Single
import javax.inject.Inject

class YoutubeRemote @Inject constructor(private val service: YoutubeService) : IYoutubeRemote {
    override fun getActivities(accessToken: String): Single<List<PlaylistItemData>> =
            service.getActivities(authorization = "Bearer $accessToken")
                    .map { it.items.filter { it.contentDetails.upload != null }.map(ApiActivityMapper::toData) }

    override fun getSubscriptions(
            accessToken: String,
            pageToken: String?
    ): Single<Pair<List<SubscriptionData>, String?>> = service.getUserSubscriptions(
            authorization = "Bearer $accessToken",
            pageToken = pageToken
    ).map { Pair(it.items.map(ApiSubscriptionMapper::toData), it.nextPageToken) }

    override fun getChannelsPlaylistIds(
            channelIds: List<String>
    ): Single<List<ChannelPlaylistIdData>> = service.getChannelsPlaylistId(ids = channelIds.joinToString())
            .map { it.items.map(ApiChannelPlaylistIdMapper::toData) }

    override fun getPlaylistItems(channelId: String): Single<List<PlaylistItemData>> = service.getPlaylistItems(id = channelId)
            .map { it.items.map(ApiPlaylistItemMapper::toData) }
}