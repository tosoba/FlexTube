package com.example.there.domain.repo

import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.model.Subscription
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface IMainRepository {
    fun getSubs(accessToken: String, accountName: String): Observable<List<Subscription>>
    fun updateSavedSubscriptions(subs: List<Subscription>, accountName: String): Completable

    fun getVideos(channelIds: List<String>): Observable<List<PlaylistItem>>
    fun getMoreVideos(channelIds: List<String>): Observable<List<PlaylistItem>>

    fun getGeneralHomeItems(accessToken: String): Single<List<PlaylistItem>>
}