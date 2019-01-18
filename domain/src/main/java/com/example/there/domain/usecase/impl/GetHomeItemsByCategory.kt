package com.example.there.domain.usecase.impl

import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Single
import javax.inject.Inject

class GetHomeItemsByCategory @Inject constructor(
        private val repository: IMainRepository
) : UseCase<GetHomeItemsByCategory.Params, Single<List<PlaylistItem>>> {
    override fun execute(params: Params?): Single<List<PlaylistItem>> = repository.getHomeItemsByCategory(
            categoryId = params!!.categoryId,
            shouldReturnAll = params.shouldReturnAll
    )

    data class Params(val categoryId: String, val shouldReturnAll: Boolean = false)
}