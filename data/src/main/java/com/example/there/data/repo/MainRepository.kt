package com.example.there.data.repo

import com.example.there.data.mapper.PlaylistItemMapper
import com.example.there.data.mapper.SubscriptionMapper
import com.example.there.data.repo.store.base.IYoutubeDataStore
import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.model.Subscription
import com.example.there.domain.repo.IMainRepository
import io.reactivex.Observable
import javax.inject.Inject

class MainRepository @Inject constructor(private val remoteYoutubeDataStore: IYoutubeDataStore): IMainRepository {
    override fun getSubs(accessToken: String): Observable<List<Subscription>> {
        return remoteYoutubeDataStore.getUserSubscriptions(accessToken).map { it.map(SubscriptionMapper::toDomain) }
    }

    override fun getVideos(channelIds: List<String>): Observable<List<PlaylistItem>> {
        return remoteYoutubeDataStore.getVideos(channelIds).map {it.map(PlaylistItemMapper::toDomain)}
    }
}