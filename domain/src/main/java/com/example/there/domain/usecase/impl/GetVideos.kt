package com.example.there.domain.usecase.impl

import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable
import javax.inject.Inject

class GetVideos @Inject constructor(private val repository: IMainRepository): ObservableUseCase<List<PlaylistItem>, List<String>> {
    override fun execute(params: List<String>?): Observable<List<PlaylistItem>> = repository.getVideos(params!!)
}