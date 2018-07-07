package com.example.there.data.repo.store.impl

import com.example.there.data.model.PlaylistItemData
import com.example.there.data.model.SubscriptionData
import com.example.there.data.repo.store.base.IYoutubeDataStore
import com.example.there.data.repo.store.base.IYoutubeRemote
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class YoutubeRemoteDataStore @Inject constructor(private val remote: IYoutubeRemote) : IYoutubeDataStore {
    override fun getUserSubscriptions(accessToken: String, accountName: String): Observable<List<SubscriptionData>> {
        val pageTokenSubject = BehaviorSubject.createDefault("")
        return pageTokenSubject.concatMap { token ->
            remote.getSubscriptions(
                    pageToken = if (token == "") null else token,
                    accessToken = accessToken
            ).toObservable()
        }.doOnNext { (_, token) ->
            if (token != null) pageTokenSubject.onNext(token)
            else pageTokenSubject.onComplete()
        }.map { (subs, _) -> subs }
    }

    override fun getVideos(channelIds: List<String>): Observable<List<PlaylistItemData>> = remote.getChannelsPlaylistIds(channelIds)
            .toObservable()
            .flatMapIterable { it }
            .flatMap { remote.getPlaylistItems(it.playlistId).toObservable() }

    override fun saveUserSubscriptions(subs: List<SubscriptionData>, accountName: String): Completable = throw UnsupportedOperationException()

    override fun saveUser(accountName: String): Completable = throw UnsupportedOperationException()

    companion object {
        const val NAME = "REMOTE_DATA_STORE"
    }
}