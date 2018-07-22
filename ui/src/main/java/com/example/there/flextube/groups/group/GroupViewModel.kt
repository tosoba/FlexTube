package com.example.there.flextube.groups.group

import android.util.Log
import com.example.there.domain.usecase.impl.GetSavedVideosWithUpdates
import com.example.there.domain.usecase.impl.GetSubscriptionsFromGroup
import com.example.there.domain.usecase.impl.LoadMoreVideos
import com.example.there.flextube.base.BaseViewModel
import com.example.there.flextube.model.UiGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GroupViewModel @Inject constructor(
        private val getSubscriptionsFromGroup: GetSubscriptionsFromGroup,
        private val getSavedVideosWithUpdates: GetSavedVideosWithUpdates,
        private val loadMoreVideos: LoadMoreVideos
) : BaseViewModel() {

    val viewState = GroupViewState()

    private var loadingInProgress = false

    fun loadData(group: UiGroup) {
        loadingInProgress = true
        disposables.add(getSubscriptionsFromGroup.execute(GetSubscriptionsFromGroup.Params(group.accountName, group.name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess {
                    loadingInProgress = false
                    viewState.subscriptions.addAll(it)
                }
                .observeOn(Schedulers.io())
                .toFlowable()
                .flatMap { getSavedVideosWithUpdates.execute(viewState.subscriptions.map { it.channelId }) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.videos.addAll(it) }, { Log.e(javaClass.name, it.message) }))
    }

    fun loadMoreVideos() {
        if (!loadingInProgress) {
            loadingInProgress = true
            disposables.add(loadMoreVideos.execute(viewState.subscriptions.map { it.channelId })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { loadingInProgress = false }
                    .subscribe({}, { Log.e(javaClass.name, it.message) }))
        }
    }
}