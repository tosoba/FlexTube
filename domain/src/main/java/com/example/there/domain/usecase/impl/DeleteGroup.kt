package com.example.there.domain.usecase.impl

import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Completable
import javax.inject.Inject

class DeleteGroup @Inject constructor(
        private val repository: IMainRepository
) : UseCase<DeleteGroup.Params, Completable> {
    override fun execute(params: Params?): Completable = repository.deleteGroup(
            groupName = params!!.groupName,
            accountName = params.accountName
    )

    class Params(
            val groupName: String,
            val accountName: String
    )
}