package com.example.there.domain.usecase.impl

import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Single
import javax.inject.Inject

class GetGeneralHomeItems @Inject constructor(private val repository: IMainRepository): UseCase<String, Single<List<PlaylistItem>>> {
    override fun execute(params: String?): Single<List<PlaylistItem>> = repository.getGeneralHomeItems(accessToken = params!!)
}