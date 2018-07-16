package com.example.there.data.repo.store

import com.example.there.data.model.HomeItemsData
import com.example.there.data.model.PlaylistData
import com.example.there.data.model.PlaylistItemData
import com.example.there.data.model.SubscriptionData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface IYoutubeCache {
    fun getUserSubscriptions(accountName: String): Observable<List<SubscriptionData>>
    fun getSubscriptionsFromGroup(groupName: String): Single<List<SubscriptionData>>
    fun saveUserSubscriptions(subs: List<SubscriptionData>, accountName: String): Completable
    fun updateSavedSubscriptions(subs: List<SubscriptionData>, accountName: String): Completable

    fun saveUser(accountName: String): Completable

    fun savePlaylist(playlistId: String, channelId: String): Completable
    fun getPlaylistByChannelId(channelId: String): Single<PlaylistData>
    fun getPlaylistById(id: String): Single<PlaylistData>
    fun updatePlaylistNextPageToken(id: String, nextPageToken: String?): Completable

    fun getSavedVideos(playlistId: String): Flowable<List<PlaylistItemData>>
    fun saveRetrievedVideos(playlistId: String, videos: List<PlaylistItemData>, nextPageToken: String? = null): Completable

    fun getSavedHomeItems(categoryId: String): Single<HomeItemsData>
    fun saveHomeItems(categoryId: String, videos: List<PlaylistItemData>, nextPageToken: String? = null)

    companion object {
        const val CATEGORY_GENERAL = "CATEGORY_GENERAL"
    }
}