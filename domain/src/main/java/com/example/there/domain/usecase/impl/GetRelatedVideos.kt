package com.example.there.domain.usecase.impl

import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Single
import javax.inject.Inject

class GetRelatedVideos @Inject constructor(
        private val repository: IMainRepository
) : UseCase<GetRelatedVideos.Params, Single<List<PlaylistItem>>> {
    override fun execute(params: Params?): Single<List<PlaylistItem>> = repository.loadRelatedVideos(
            videoId = params!!.videoId,
            shouldReturnAll = params.shouldReturnAll
    )

    class Params(
            val videoId: String,
            val shouldReturnAll: Boolean
    )
}