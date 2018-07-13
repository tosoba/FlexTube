package com.example.there.data.repo.store

import com.example.there.data.model.ChannelPlaylistIdData
import com.example.there.data.model.PlaylistItemData
import com.example.there.data.model.SubscriptionData
import io.reactivex.Observable
import io.reactivex.Single

interface IYoutubeRemote {
    fun getHomeItems(accessToken: String, pageToken: String? = null): Single<Pair<List<PlaylistItemData>, String?>>
    fun getChannelsPlaylistIds(channelIds: List<String>): Single<List<ChannelPlaylistIdData>>
    fun getPlaylistItems(channelId: String, pageToken: String? = null): Single<Pair<List<PlaylistItemData>, String?>>
    fun getUserSubscriptions(accessToken: String): Observable<List<SubscriptionData>>
}