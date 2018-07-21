package com.example.there.data.repo.store

import com.example.there.data.model.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface IYoutubeCache {
    fun getUserSubscriptions(accountName: String): Flowable<List<SubscriptionData>>
    fun saveUserSubscriptions(subs: List<SubscriptionData>, accountName: String): Completable
    fun updateSavedSubscriptions(subs: List<SubscriptionData>, accountName: String): Completable
    fun getSubscriptionsFromGroup(accountName: String, groupName: String): Single<List<SubscriptionData>>

    fun saveUser(accountName: String): Completable

    fun savePlaylist(playlistId: String, channelId: String): Completable
    fun getPlaylistByChannelId(channelId: String): Single<PlaylistData>
    fun getPlaylistById(id: String): Single<PlaylistData>
    fun updatePlaylistNextPageToken(id: String, nextPageToken: String?): Completable

    fun getSavedVideos(channelId: String): Flowable<List<PlaylistItemData>>
    fun saveRetrievedVideos(playlistId: String, videos: List<PlaylistItemData>, nextPageToken: String? = null): Completable

    fun getSavedHomeItems(categoryId: String): Single<HomeItemsData>
    fun saveHomeItems(categoryId: String, videos: List<PlaylistItemData>, nextPageToken: String? = null)

    fun getGroupsForAccount(accountName: String): Flowable<List<GroupData>>
    fun getGroup(groupName: String, accountName: String): Single<GroupData>

    fun insertGroupWithSubscriptions(groupName: String, accountName: String, subscriptionIds: List<String>): Completable

    companion object {
        const val CATEGORY_GENERAL = "CATEGORY_GENERAL"
    }
}