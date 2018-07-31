package com.example.there.domain.usecase.impl

import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Single
import javax.inject.Inject

class SearchForVideos @Inject constructor(
        private val repository: IMainRepository
) : UseCase<SearchForVideos.Params, Single<List<PlaylistItem>>> {
    override fun execute(params: SearchForVideos.Params?): Single<List<PlaylistItem>> = repository.searchForVideos(
            query = params!!.query,
            shouldReturnAll = params.shouldReturnAll
    )

    class Params(val query: String, val shouldReturnAll: Boolean)
}