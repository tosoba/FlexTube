package com.example.there.domain.usecase.impl

import com.example.there.domain.model.Group
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Flowable
import javax.inject.Inject

class GetUserGroups @Inject constructor(
        private val repository: IMainRepository
) : UseCase<String, Flowable<List<Group>>> {
    override fun execute(params: String?): Flowable<List<Group>> = repository.getGroups(accountName = params!!)
}