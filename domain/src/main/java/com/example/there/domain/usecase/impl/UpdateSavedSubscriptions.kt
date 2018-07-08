package com.example.there.domain.usecase.impl

import com.example.there.domain.model.Subscription
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Completable
import javax.inject.Inject

class UpdateSavedSubscriptions @Inject constructor(
        private val repository: IMainRepository
) : UseCase<UpdateSavedSubscriptions.Params, Completable> {

    override fun execute(params: Params?): Completable = repository.updateSavedSubscriptions(params!!.subs, params.accountName)

    data class Params(
            val accountName: String,
            val subs: List<Subscription>
    )
}