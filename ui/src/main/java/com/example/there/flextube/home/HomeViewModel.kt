package com.example.there.flextube.home

import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.usecase.impl.GetHomePlaylistItems
import com.example.there.flextube.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val getHomePlaylistItems: GetHomePlaylistItems) : BaseViewModel() {
    val homeItems: PublishSubject<List<PlaylistItem>> = PublishSubject.create()

    fun loadHomeItems(accessToken: String) {
        disposables.add(getHomePlaylistItems.execute(params = accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    homeItems.onNext(it)
                }, {}))
    }
}