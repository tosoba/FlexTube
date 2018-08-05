package com.example.there.flextube.search

import android.util.Log
import com.example.there.domain.usecase.impl.SearchForVideos
import com.example.there.flextube.base.vm.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel @Inject constructor(
        private val searchForVideos: SearchForVideos
) : BaseViewModel() {

    val viewState = SearchViewState()

    private var loadingInProgress = false

    fun searchVideos(query: String, shouldReturnAll: Boolean, onFinally: () -> Unit = {}) {
        if (!loadingInProgress) {
            loadingInProgress = true
            disposables.add(searchForVideos.execute(SearchForVideos.Params(query, shouldReturnAll))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally {
                        loadingInProgress = false
                        onFinally()
                    }
                    .subscribe({ viewState.foundVideos.addAll(it) }, {
                        Log.e("ERR", it.message ?: "searchVideos")
                    }))
        }
    }
}