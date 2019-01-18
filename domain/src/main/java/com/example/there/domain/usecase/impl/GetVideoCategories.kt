package com.example.there.domain.usecase.impl

import com.example.there.domain.model.VideoCategory
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Single
import javax.inject.Inject

class GetVideoCategories @Inject constructor(
        private val repository: IMainRepository
) : UseCase<Unit, Single<List<VideoCategory>>> {
    override fun execute(params: Unit?): Single<List<VideoCategory>> = repository.getVideoCategories()
}