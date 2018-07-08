package com.example.there.data.repo.store.base

import com.example.there.data.model.ChannelPlaylistIdData
import com.example.there.data.model.PlaylistItemData
import com.example.there.data.model.SubscriptionData
import io.reactivex.Single

interface IYoutubeRemote {
    fun getSubscriptions(accessToken: String, pageToken: String? = null): Single<Pair<List<SubscriptionData>, String?>>
    fun getChannelsPlaylistIds(channelIds: List<String>): Single<List<ChannelPlaylistIdData>>
    fun getPlaylistItems(channelId: String): Single<List<PlaylistItemData>>
    fun getActivities(accessToken: String): Single<List<PlaylistItemData>>
}