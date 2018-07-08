package com.example.there.domain.usecase.impl

import com.example.there.domain.model.Subscription
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.UseCase
import io.reactivex.Observable
import javax.inject.Inject

class GetUserSubscriptions @Inject constructor(
        private val repository: IMainRepository
) : UseCase<GetUserSubscriptions.Params, Observable<List<Subscription>>> {

    override fun execute(params: Params?): Observable<List<Subscription>> = repository.getSubs(
            accessToken = params!!.accessToken,
            accountName = params.accountName
    )

    data class Params(val accessToken: String, val accountName: String)
}