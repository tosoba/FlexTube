package com.example.there.domain.repo

import com.example.there.domain.model.Group
import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.model.Subscription
import com.example.there.domain.model.VideoCategory
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface IMainRepository {
    fun getSubscriptions(accessToken: String, accountName: String): Observable<List<Subscription>>
    fun getSavedSubsriptions(accountName: String): Flowable<List<Subscription>>
    fun updateSavedSubscriptions(subs: List<Subscription>, accountName: String): Completable
    fun getSubscriptionsFromGroup(accountName: String, groupName: String): Single<List<Subscription>>
    fun getGroups(accountName: String): Flowable<List<Group>>

    fun loadVideos(channelIds: List<String>): Observable<List<PlaylistItem>>
    fun loadMoreVideos(channelIds: List<String>): Completable
    fun getSavedVideosWithUpdates(channelIds: List<String>): Flowable<List<PlaylistItem>>
    fun getSavedVideos(channelIds: List<String>): Single<List<PlaylistItem>>

    fun loadRelatedVideos(videoId: String, shouldReturnAll: Boolean = false): Single<List<PlaylistItem>>

    fun getGeneralHomeItems(accessToken: String, shouldReturnAll: Boolean = false): Single<List<PlaylistItem>>
    fun getHomeItemsByCategory(categoryId: String, shouldReturnAll: Boolean = false): Single<List<PlaylistItem>>

    fun getVideoCategories(): Single<List<VideoCategory>>
    fun getGroup(groupName: String, accountName: String): Single<Group>

    fun insertGroupWithSubscriptions(groupName: String, accountName: String, subscriptionIds: List<String>): Completable
}