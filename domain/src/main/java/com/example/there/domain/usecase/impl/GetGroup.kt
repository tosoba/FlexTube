package com.example.there.domain.usecase.impl

import com.example.there.domain.model.Group
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Single
import javax.inject.Inject

class GetGroup @Inject constructor(
        private val repository: IMainRepository
) : UseCase<GetGroup.Params, Single<Group>> {
    override fun execute(params: Params?): Single<Group> = repository.getGroup(
            groupName = params!!.groupName,
            accountName = params.accountName
    )

    class Params(val accountName: String, val groupName: String)
}