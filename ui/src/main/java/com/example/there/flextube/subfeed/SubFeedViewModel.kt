package com.example.there.flextube.subfeed

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.there.domain.model.Subscription
import com.example.there.domain.usecase.impl.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SubFeedViewModel @Inject constructor(
        private val getUserSubscriptions: GetUserSubscriptions,
        private val updateSavedSubscriptions: UpdateSavedSubscriptions,
        private val getVideos: GetVideos,
        private val loadMoreVideos: LoadMoreVideos,
        private val getSavedVideos: GetSavedVideos
) : ViewModel() {

    private val disposables = CompositeDisposable()

    val viewState = SubFeedViewState()

    fun loadVideos(accessToken: String, accountName: String) {
        loadingVideosInProgress = true
        disposables.add(getUserSubscriptions
                .execute(GetUserSubscriptions.Params(accessToken, accountName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { viewState.subscriptions.addAll(it) }
                .doOnComplete { updateDbSubscriptions(viewState.subscriptions, accountName) }
                .observeOn(Schedulers.io())
                .flatMap { subs -> getVideos.execute(subs.map { it.channelId }) }
                .doOnComplete {
                    loadingVideosInProgress = false
                    bindDbVideos()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.videos.addAll(it) }, { Log.e("ERR", it.message) }))
    }

    private var loadingVideosInProgress = false

    private fun bindDbVideos() {
        disposables.add(getSavedVideos.execute(viewState.subscriptions.map { it.channelId })
                .subscribeOn(Schedulers.io())
                .map { it.filter { !viewState.videos.contains(it) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.videos.addAll(it) }, { Log.e("ERR", it.message) }))
    }

    fun loadMoreVideos() {
        if (!loadingVideosInProgress) {
            loadingVideosInProgress = true
            disposables.add(loadMoreVideos.execute(viewState.subscriptions.map { it.channelId.trim() })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { loadingVideosInProgress = false }
                    .subscribe())
        }
    }

    private fun updateDbSubscriptions(
            subs: List<Subscription>,
            accountName: String
    ) = disposables.add(updateSavedSubscriptions
            .execute(UpdateSavedSubscriptions.Params(accountName, subs))
            .subscribeOn(Schedulers.io())
            .subscribe())

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}