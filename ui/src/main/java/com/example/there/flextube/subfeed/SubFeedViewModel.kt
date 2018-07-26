package com.example.there.flextube.subfeed

import android.util.Log
import com.example.there.domain.model.Subscription
import com.example.there.domain.usecase.impl.*
import com.example.there.flextube.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SubFeedViewModel @Inject constructor(
        private val loadUserSubscriptions: LoadUserSubscriptions,
        private val updateSavedSubscriptions: UpdateSavedSubscriptions,
        private val getVideos: GetVideos,
        private val loadMoreVideos: LoadMoreVideos,
        private val getSavedVideosWithUpdates: GetSavedVideosWithUpdates,
        private val getSavedVideos: GetSavedVideos
) : BaseViewModel() {

    val viewState = SubFeedViewState()

    private var loadingVideosInProgress = false

    fun loadVideos(accessToken: String, accountName: String) {
        loadingVideosInProgress = true
        disposables.add(loadUserSubscriptions
                .execute(LoadUserSubscriptions.Params(accessToken, accountName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { viewState.subscriptions.addAll(it) }
                .doOnComplete {
                    updateDbSubscriptions(viewState.subscriptions, accountName)
                    loadDbVideos()
                }
                .observeOn(Schedulers.io())
                .flatMap { subs -> getVideos.execute(subs.map { it.channelId }) }
                .doOnComplete {
                    loadingVideosInProgress = false
                    bindDbVideos()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.filter { !viewState.videos.contains(it) } }
                .subscribe({ viewState.videos.addAll(it) }, { Log.e("ERR", it.message) }))
    }

    private fun loadDbVideos() {
        disposables.add(getSavedVideos.execute(viewState.subscriptions.map { it.channelId })
                .subscribeOn(Schedulers.io())
                .map { it.filter { !viewState.videos.contains(it) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.videos.addAll(it) }, { Log.e("ERR", it.message ?: "No saved videos.") }))
    }

    private fun bindDbVideos() {
        disposables.add(getSavedVideosWithUpdates.execute(viewState.subscriptions.map { it.channelId })
                .subscribeOn(Schedulers.io())
                .map { it.filter { !viewState.videos.contains(it) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.videos.addAll(it) }, { Log.e("ERR", it.message ?: "No saved videos.") }))
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
}