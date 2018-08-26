package com.example.there.flextube.subfeed

import android.util.Log
import com.example.there.domain.model.Subscription
import com.example.there.domain.usecase.impl.*
import com.example.there.flextube.base.vm.BaseViewModel
import com.example.there.flextube.mapper.UiSubscriptionMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SubFeedViewModel @Inject constructor(
        private val loadUserSubscriptions: LoadUserSubscriptions,
        private val updateSavedSubscriptions: UpdateSavedSubscriptions,
        private val loadVideos: LoadVideos,
        private val loadMoreVideos: LoadMoreVideos,
        private val getSavedVideosWithUpdates: GetSavedVideosWithUpdates,
        private val getSavedVideos: GetSavedVideos
) : BaseViewModel() {

    val viewState = SubFeedViewState()

    private var loadingVideosInProgress = false

    var loadingRemoteVideosComplete = false
        private set

    fun loadData(accessToken: String, accountName: String, reloadAfterConnectionLoss: Boolean = false, onAfterAdd: (() -> Unit)? = null) {
        if (!loadingVideosInProgress || reloadAfterConnectionLoss) {
            loadingVideosInProgress = true
            disposables.add(loadUserSubscriptions
                    .execute(LoadUserSubscriptions.Params(accessToken, accountName))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { viewState.subscriptions.addAll(it.map(UiSubscriptionMapper::toUi)) }
                    .doOnComplete {
                        if (viewState.subscriptions.isNotEmpty()) {
                            updateDbSubscriptions(viewState.subscriptions.map(UiSubscriptionMapper::toDomain), accountName)
                            loadDbVideos(onAfterAdd)
                        } else {
                            viewState.noSubscriptions.set(true)
                        }
                    }
                    .observeOn(Schedulers.io())
                    .flatMap { subs -> loadVideos.execute(subs.map { it.channelId }) }
                    .doOnComplete {
                        loadingRemoteVideosComplete = true
                        loadingVideosInProgress = false
                        if (viewState.subscriptions.isNotEmpty()) {
                            bindDbVideos()
                        }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { it.filter { !viewState.videos.contains(it) } }
                    .subscribe({ viewState.videos.addAll(it) }, { Log.e("ERR", it.message) }))
        }
    }

    fun refreshVideos(stopRefreshing: () -> Unit) {
        if (!loadingVideosInProgress) {
            loadingVideosInProgress = true
            disposables.add(loadVideos.execute(viewState.subscriptions.map { it.channelId })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally {
                        loadingVideosInProgress = false
                        stopRefreshing()
                    }
                    .subscribe({}, { Log.e("ERR", it.message) }))
        } else {
            stopRefreshing()
        }
    }

    private fun loadDbVideos(onAfterAdd: (() -> Unit)? = null) {
        disposables.add(getSavedVideos.execute(viewState.subscriptions.map { it.channelId })
                .subscribeOn(Schedulers.io())
                .map { it.filter { !viewState.videos.contains(it) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.videos.addAll(it)
                    onAfterAdd?.invoke()
                }, {
                    Log.e("ERR", it.message ?: "No saved videos.")
                }))
    }

    private fun bindDbVideos() {
        disposables.add(getSavedVideosWithUpdates.execute(viewState.subscriptions.map { it.channelId })
                .subscribeOn(Schedulers.io())
                .map { it.filter { !viewState.videos.contains(it) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.videos.addAll(it) }, {
                    Log.e("ERR", it.message ?: "No saved videos.")
                }))
    }

    fun loadMoreVideos(onFinally: () -> Unit) {
        if (!loadingVideosInProgress) {
            loadingVideosInProgress = true
            disposables.add(loadMoreVideos.execute(viewState.subscriptions.map { it.channelId.trim() })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { loadingVideosInProgress = false }
                    .doFinally(onFinally)
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