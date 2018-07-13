package com.example.there.flextube.home

import com.example.there.domain.usecase.impl.GetGeneralHomeItems
import com.example.there.flextube.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val getGeneralHomeItems: GetGeneralHomeItems) : BaseViewModel() {
    val viewState = HomeViewState()

    fun loadHomeItems(accessToken: String) {
        if (viewState.isLoadingInProgress.get() == false) {
            viewState.isLoadingInProgress.set(true)
            disposables.add(getGeneralHomeItems.execute(params = accessToken)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally { viewState.isLoadingInProgress.set(false) }
                    .subscribe({ viewState.homeItems.addAll(it) }, {}))
        }
    }
}