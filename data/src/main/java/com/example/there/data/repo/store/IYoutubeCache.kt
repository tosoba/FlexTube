package com.example.there.data.repo.store

import com.example.there.data.model.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface IYoutubeCache {
    fun getUserSubscriptions(accountName: String): Flowable<List<SubscriptionData>>
    fun saveUserSubscriptions(subs: List<SubscriptionData>, accountName: String): Completable
    fun updateSavedSubscriptions(subs: List<SubscriptionData>, accountName: String): Completable
    fun getSubscriptionsFromGroup(accountName: String, groupName: String): Flowable<List<SubscriptionData>>
    fun getSubscriptionsNotAddedToGroup(accountName: String, groupName: String): Flowable<List<SubscriptionData>>

    fun saveUser(accountName: String): Completable

    fun savePlaylist(playlistId: String, channelId: String): Completable
    fun getPlaylistByChannelId(channelId: String): Single<PlaylistData>
    fun getPlaylistById(id: String): Single<PlaylistData>
    fun updatePlaylistNextPageToken(id: String, nextPageToken: String?): Completable

    fun getSavedVideosFlowable(channelId: String): Flowable<List<PlaylistItemData>>
    fun getSavedVideos(channelId: String): Single<List<PlaylistItemData>>
    fun saveRetrievedVideos(playlistId: String, videos: List<PlaylistItemData>, nextPageToken: String? = null): Completable

    fun getSavedHomeItems(categoryId: String): Single<SavedPlaylistItemsData>
    fun saveHomeItems(categoryId: String, videos: List<PlaylistItemData>, nextPageToken: String? = null)

    fun getGroupsForAccount(accountName: String): Flowable<List<GroupData>>
    fun getGroup(groupName: String, accountName: String): Single<GroupData>
    fun deleteGroup(groupName: String, accountName: String): Completable
    fun insertGroupWithSubscriptions(groupName: String, accountName: String, subscriptionIds: List<String>): Completable
    fun addSubscriptionsToGroup(groupName: String, accountName: String, subscriptionIds: List<String>): Completable

    fun saveRelatedVideos(videoId: String, videos: List<PlaylistItemData>, nextPageToken: String? = null)
    fun getSavedRelatedVideos(videoId: String): Single<SavedPlaylistItemsData>

    fun saveFoundVideos(query: String, videos: List<PlaylistItemData>, nextPageToken: String? = null)
    fun getSavedFoundVideos(query: String): Single<SavedPlaylistItemsData>

    companion object {
        const val CATEGORY_GENERAL = "CATEGORY_GENERAL"
    }
}