package com.example.there.domain.usecase.impl

import com.example.there.domain.model.Subscription
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Flowable
import javax.inject.Inject

class GetSubscriptionsFromGroup @Inject constructor(
        private val repository: IMainRepository
) : UseCase<GetSubscriptionsFromGroup.Params, Flowable<List<Subscription>>> {
    override fun execute(params: Params?): Flowable<List<Subscription>> = repository.getSubscriptionsFromGroup(
            accountName = params!!.accountName,
            groupName = params.groupName
    )

    data class Params(val accountName: String, val groupName: String)
}