package com.example.there.domain.usecase.impl

import com.example.there.domain.model.Subscription
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Single
import javax.inject.Inject

class GetSubscriptionsFromGroup @Inject constructor(
        private val repository: IMainRepository
): UseCase<GetSubscriptionsFromGroup.Params, Single<List<Subscription>>> {
    override fun execute(params: Params?): Single<List<Subscription>> = repository.getSubscriptionsFromGroup(
            accountName = params!!.accountName,
            groupName = params.groupName
    )

    data class Params(val accountName: String, val groupName: String)
}