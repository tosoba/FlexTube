package com.example.there.flextube.home

import android.databinding.ObservableField
import android.util.Log
import com.example.there.data.repo.store.IYoutubeCache
import com.example.there.domain.usecase.impl.GetGeneralHomeItems
import com.example.there.domain.usecase.impl.GetHomeItemsByCategory
import com.example.there.domain.usecase.impl.GetVideoCategories
import com.example.there.flextube.R
import com.example.there.flextube.base.vm.BaseViewModel
import com.example.there.flextube.mapper.UiVideoCategoryMapper
import com.example.there.flextube.model.UiVideoCategory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor(
        private val getGeneralHomeItems: GetGeneralHomeItems,
        private val getHomeItemsByCategory: GetHomeItemsByCategory,
        private val getVideoCategories: GetVideoCategories
) : BaseViewModel() {

    val viewState = HomeViewState()

    var loadingGeneralHomeItemsComplete = false
        private set

    fun loadGeneralHomeItems(accessToken: String, shouldReturnAll: Boolean = false, onFinally: () -> Unit = {}, onAfterAdd: (() -> Unit)? = null) {
        if (viewState.isLoadingInProgress.get() == false) {
            previousCategoryId = IYoutubeCache.CATEGORY_GENERAL
            if (shouldReturnAll) viewState.homeItems.clear()
            viewState.isLoadingInProgress.set(true)
            disposables.add(getGeneralHomeItems.execute(params = GetGeneralHomeItems.Params(accessToken, shouldReturnAll))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { loadingGeneralHomeItemsComplete = true }
                    .doFinally {
                        viewState.isLoadingInProgress.set(false)
                        onFinally()
                    }
                    .subscribe({
                        viewState.homeItems.addAll(it)
                        onAfterAdd?.invoke()
                    }, { Log.e("ERR", it.message) }))
        }
    }

    private var previousCategoryId: String? = null

    fun loadHomeItemsByCategory(categoryId: String, shouldReturnAll: Boolean = true, onFinally: () -> Unit = {}) {
        fun load() {
            previousCategoryId = categoryId

            if (shouldReturnAll) viewState.homeItems.clear()
            viewState.isLoadingInProgress.set(true)

            disposables.add(getHomeItemsByCategory.execute(params = GetHomeItemsByCategory.Params(categoryId, shouldReturnAll))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally {
                        viewState.isLoadingInProgress.set(false)
                        onFinally()
                    }
                    .subscribe({ viewState.homeItems.addAll(it) }, { Log.e("ERR", it.message) }))
        }

        if (previousCategoryId == null || previousCategoryId != categoryId) {
            disposables.clear()
            load()
        } else if (viewState.isLoadingInProgress.get() == false) {
            load()
        }
    }

    fun loadVideoCategories() {
        disposables.add(getVideoCategories.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.videoCategories.add(
                            UiVideoCategory(
                                    IYoutubeCache.CATEGORY_GENERAL,
                                    "General",
                                    R.drawable.home_white, ObservableField(true)
                            )
                    )
                    viewState.videoCategories.addAll(it.map(UiVideoCategoryMapper::toUi))
                }, { Log.e("ERR", it.message) }))
    }
}