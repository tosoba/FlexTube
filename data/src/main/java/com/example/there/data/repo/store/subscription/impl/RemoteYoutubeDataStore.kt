package com.example.there.data.repo.store.subscription.impl

import com.example.there.data.model.SubscriptionData
import com.example.there.data.repo.store.subscription.base.IYoutubeDataStore
import com.example.there.data.repo.store.subscription.base.IYoutubeRemote
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class RemoteYoutubeDataStore @Inject constructor(private val remote: IYoutubeRemote) : IYoutubeDataStore {
    override fun getUserSubscriptions(accessToken: String): Observable<List<SubscriptionData>> {
        val pageTokenSubject = BehaviorSubject.createDefault("")
        return pageTokenSubject.concatMap { token ->
            val pageToken = if (token == "") null else token
            remote.getSubscriptions(pageToken = pageToken, accessToken = accessToken).toObservable()
        }.doOnNext { (_, token) ->
            if (token != null) pageTokenSubject.onNext(token)
            else pageTokenSubject.onComplete()
        }.map { (subs, _) ->
            subs
        }
    }

    override fun saveUserSubscriptions(subs: List<SubscriptionData>): Completable = throw UnsupportedOperationException()
}