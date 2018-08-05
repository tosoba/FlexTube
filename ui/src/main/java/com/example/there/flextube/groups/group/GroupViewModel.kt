package com.example.there.flextube.groups.group

import android.util.Log
import com.example.there.domain.usecase.impl.DeleteGroup
import com.example.there.domain.usecase.impl.GetSavedVideosWithUpdates
import com.example.there.domain.usecase.impl.GetSubscriptionsFromGroup
import com.example.there.domain.usecase.impl.LoadMoreVideos
import com.example.there.flextube.base.vm.BaseViewModel
import com.example.there.flextube.model.UiGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GroupViewModel @Inject constructor(
        private val getSubscriptionsFromGroup: GetSubscriptionsFromGroup,
        private val getSavedVideosWithUpdates: GetSavedVideosWithUpdates,
        private val loadMoreVideos: LoadMoreVideos,
        private val deleteGroup: DeleteGroup
) : BaseViewModel() {

    val viewState = GroupViewState()

    private var loadingInProgress = false

    fun loadData(group: UiGroup, onAfterAdd: (() -> Unit)? = null) {
        loadingInProgress = true
        disposables.add(getSubscriptionsFromGroup.execute(GetSubscriptionsFromGroup.Params(group.accountName, group.name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.filter { !viewState.subscriptions.map { it.id }.contains(it.id) } }
                .doAfterNext {
                    loadingInProgress = false
                    viewState.subscriptions.addAll(it)
                }
                .observeOn(Schedulers.io())
                .flatMap { getSavedVideosWithUpdates.execute(viewState.subscriptions.map { it.channelId }) }
                .observeOn(AndroidSchedulers.mainThread())
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

    fun deleteGroup(group: UiGroup, onComplete: () -> Unit) {
        disposables.clear()
        disposables.add(deleteGroup.execute((DeleteGroup.Params(groupName = group.name, accountName = group.accountName)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onComplete() }, { Log.e(javaClass.name, it.message) }))
    }
}