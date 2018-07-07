package com.example.there.presentation.subfeed

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.model.Subscription
import com.example.there.domain.usecase.impl.GetUserSubscriptions
import com.example.there.domain.usecase.impl.GetVideos
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SubFeedViewModel @Inject constructor(
        private val getUserSubscriptions: GetUserSubscriptions,
        private val getVideos: GetVideos
) : ViewModel() {

    private val disposables = CompositeDisposable()

    val subscriptions: PublishSubject<List<Subscription>> = PublishSubject.create()
    val videos: PublishSubject<List<PlaylistItem>> = PublishSubject.create()

    fun loadVideos(accessToken: String) = disposables.add(getUserSubscriptions.execute(accessToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { subscriptions.onNext(it) }
            .observeOn(Schedulers.io())
            .flatMap { subs -> getVideos.execute(subs.map { it.channelId }) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ videos.onNext(it) }, { Log.e("ERR", it.message) }))

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}