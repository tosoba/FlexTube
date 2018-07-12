package com.example.there.flextube.subfeed

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.there.domain.model.Subscription
import com.example.there.domain.usecase.impl.GetMoreVideos
import com.example.there.domain.usecase.impl.GetUserSubscriptions
import com.example.there.domain.usecase.impl.GetVideos
import com.example.there.domain.usecase.impl.UpdateSavedSubscriptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SubFeedViewModel @Inject constructor(
        private val getUserSubscriptions: GetUserSubscriptions,
        private val getVideos: GetVideos,
        private val getMoreVideos: GetMoreVideos,
        private val updateSavedSubscriptions: UpdateSavedSubscriptions
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
                .doOnComplete { loadingVideosInProgress = false }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.videos.addAll(it) }, { Log.e("ERR", it.message) }))
    }

    private var loadingVideosInProgress = false

    fun loadMoreVideos() {
        if (!loadingVideosInProgress) {
            loadingVideosInProgress = true
            disposables.add(getMoreVideos.execute(viewState.subscriptions.map { it.channelId.trim() })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { loadingVideosInProgress = false }
                    .subscribe({ viewState.videos.addAll(it) }, { Log.e("ERR", it.message) }))
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