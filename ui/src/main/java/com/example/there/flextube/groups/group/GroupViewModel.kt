package com.example.there.flextube.groups.group

import android.util.Log
import com.example.there.domain.usecase.impl.GetSavedVideos
import com.example.there.domain.usecase.impl.GetSubscriptionsFromGroup
import com.example.there.flextube.base.BaseViewModel
import com.example.there.flextube.model.UiGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GroupViewModel @Inject constructor(
        private val getSubscriptionsFromGroup: GetSubscriptionsFromGroup,
        private val getSavedVideos: GetSavedVideos
) : BaseViewModel() {

    val viewState = GroupViewState()

    fun loadData(group: UiGroup) {
        disposables.add(getSubscriptionsFromGroup.execute(GetSubscriptionsFromGroup.Params(group.accountName, group.name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess { viewState.subscriptions.addAll(it) }
                .observeOn(Schedulers.io())
                .toFlowable()
                .flatMap { getSavedVideos.execute(viewState.subscriptions.map { it.channelId }) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.videos.addAll(it) }, { Log.e(javaClass.name, it.message) }))
    }
}