package com.example.there.domain.usecase.impl

import com.example.there.domain.model.Subscription
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Flowable
import javax.inject.Inject

class GetSubscriptionsNotAddedToGroup @Inject constructor(
        private val repository: IMainRepository
) : UseCase<GetSubscriptionsNotAddedToGroup.Params, Flowable<List<Subscription>>> {
    override fun execute(params: Params?): Flowable<List<Subscription>> = repository.getSubscriptionsNotFromGroup(
            accountName = params!!.accountName,
            groupName = params.groupName
    )

    class Params(
            val accountName: String,
            val groupName: String
    )
}