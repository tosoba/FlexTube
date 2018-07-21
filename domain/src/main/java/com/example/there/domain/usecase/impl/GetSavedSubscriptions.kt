package com.example.there.domain.usecase.impl

import com.example.there.domain.model.Subscription
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Flowable
import javax.inject.Inject

class GetSavedSubscriptions @Inject constructor(
        private val repository: IMainRepository
) : UseCase<String, Flowable<List<Subscription>>> {
    override fun execute(params: String?): Flowable<List<Subscription>> = repository.getSavedSubsriptions(accountName = params!!)
}