package com.example.there.data.repo

import com.example.there.data.mapper.PlaylistItemMapper
import com.example.there.data.mapper.SubscriptionMapper
import com.example.there.data.repo.store.base.IYoutubeDataStore
import com.example.there.data.repo.store.impl.YoutubeCachedDataStore
import com.example.there.data.repo.store.impl.YoutubeRemoteDataStore
import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.model.Subscription
import com.example.there.domain.repo.IMainRepository
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class MainRepository @Inject constructor(
        @Named(YoutubeRemoteDataStore.NAME)
        private val youtubeRemoteDataStore: IYoutubeDataStore,

        @Named(YoutubeCachedDataStore.NAME)
        private val youtubeCachedDataStore: IYoutubeDataStore
) : IMainRepository {

    override fun getSubs(accessToken: String, accountName: String): Observable<List<Subscription>> {
        return youtubeCachedDataStore.saveUser(accountName)
                .andThen(youtubeRemoteDataStore.getUserSubscriptions(accessToken, accountName)
                        .doOnNext { youtubeCachedDataStore.saveUserSubscriptions(it, accountName) }
                        .map { it.map(SubscriptionMapper::toDomain) })
    }

    override fun getVideos(channelIds: List<String>): Observable<List<PlaylistItem>> = youtubeRemoteDataStore.getVideos(channelIds)
            .map { it.map(PlaylistItemMapper::toDomain) }
}