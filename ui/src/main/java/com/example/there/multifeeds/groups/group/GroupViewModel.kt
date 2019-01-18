package com.example.there.multifeeds.groups.group

import android.util.Log
import com.example.there.domain.usecase.impl.DeleteGroup
import com.example.there.domain.usecase.impl.GetSavedVideosWithUpdates
import com.example.there.domain.usecase.impl.LoadMoreVideos
import com.example.there.multifeeds.base.vm.BaseViewModel
import com.example.there.multifeeds.model.UiGroupWithSubscriptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GroupViewModel @Inject constructor(
        private val getSavedVideosWithUpdates: GetSavedVideosWithUpdates,
        private val loadMoreVideos: LoadMoreVideos,
        private val deleteGroup: DeleteGroup
) : BaseViewModel() {

    val viewState = GroupViewState()

    private var loadingInProgress = false

    fun loadData(group: UiGroupWithSubscriptions, onAfterAdd: (() -> Unit)? = null) {
        loadingInProgress = true
        viewState.subscriptions.addAll(group.subscriptions)

        disposables.add(getSavedVideosWithUpdates.execute(viewState.subscriptions.map { it.channelId })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterNext { loadingInProgress = false }
                .subscribe({
                    viewState.videos.addAll(it)
                    onAfterAdd?.invoke()
                }, { Log.e(javaClass.name, it.message) }))
    }

    fun loadMoreVideos(onFinally: () -> Unit) {
        if (!loadingInProgress) {
            loadingInProgress = true
            disposables.add(loadMoreVideos.execute(viewState.subscriptions.map { it.channelId })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { loadingInProgress = false }
                    .doFinally(onFinally)
                    .subscribe({}, { Log.e(javaClass.name, it.message) }))
        }
    }

    fun deleteGroup(group: UiGroupWithSubscriptions, onComplete: () -> Unit) {
        disposables.clear()
        disposables.add(deleteGroup.execute((DeleteGroup.Params(groupName = group.name, accountName = group.accountName)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onComplete() }, { Log.e(javaClass.name, it.message) }))
    }
}