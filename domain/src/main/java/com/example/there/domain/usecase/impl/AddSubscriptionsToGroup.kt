package com.example.there.domain.usecase.impl

import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Completable
import javax.inject.Inject

class AddSubscriptionsToGroup @Inject constructor(
        private val repository: IMainRepository
) : UseCase<AddSubscriptionsToGroup.Params, Completable> {
    override fun execute(params: Params?): Completable = repository.addSubscriptionsToGroup(
            groupName = params!!.groupName,
            accountName = params.accountName,
            subscriptionIds = params.subscriptionIds
    )

    class Params(
            val groupName: String,
            val accountName: String,
            val subscriptionIds: List<String>
    )
}