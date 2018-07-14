package com.example.there.flextube.home

import com.example.there.domain.usecase.impl.GetGeneralHomeItems
import com.example.there.domain.usecase.impl.GetVideoCategories
import com.example.there.flextube.base.BaseViewModel
import com.example.there.flextube.mapper.UiVideoCategoryMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor(
        private val getGeneralHomeItems: GetGeneralHomeItems,
        private val getVideoCategories: GetVideoCategories
) : BaseViewModel() {

    val viewState = HomeViewState()

    fun loadGeneralHomeItems(accessToken: String, shouldReturnAll: Boolean = false) {
        if (viewState.isLoadingInProgress.get() == false) {
            viewState.isLoadingInProgress.set(true)
            disposables.add(getGeneralHomeItems.execute(params = GetGeneralHomeItems.Params(accessToken, shouldReturnAll))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { viewState.isLoadingInProgress.set(false) }
                    .subscribe({ viewState.homeItems.addAll(it) }, {}))
        }
    }

    fun loadVideoCategories() {
        disposables.add(getVideoCategories.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.videoCategories.addAll(it.map(UiVideoCategoryMapper::toUi)) }, {}))
    }
}