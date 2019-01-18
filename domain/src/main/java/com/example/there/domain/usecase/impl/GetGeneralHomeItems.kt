package com.example.there.domain.usecase.impl

import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Single
import javax.inject.Inject

class GetGeneralHomeItems @Inject constructor(
        private val repository: IMainRepository
) : UseCase<GetGeneralHomeItems.Params, Single<List<PlaylistItem>>> {
    override fun execute(params: Params?): Single<List<PlaylistItem>> = repository.getGeneralHomeItems(
            accessToken = params!!.accessToken,
            shouldReturnAll = params.shouldReturnAll
    )

    data class Params(val accessToken: String, val shouldReturnAll: Boolean = false)
}