package com.example.there.domain.usecase.impl

import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Completable
import javax.inject.Inject

class LoadMoreVideos @Inject constructor(
        private val repository: IMainRepository
) : UseCase<List<String>, Completable> {
    override fun execute(params: List<String>?): Completable = repository.loadMoreVideos(params!!)
}