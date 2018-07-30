package com.example.there.domain.usecase.impl

import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Observable
import javax.inject.Inject

class LoadVideos @Inject constructor(
        private val repository: IMainRepository
) : UseCase<List<String>, Observable<List<PlaylistItem>>> {
    override fun execute(params: List<String>?): Observable<List<PlaylistItem>> = repository.loadVideos(params!!)
}