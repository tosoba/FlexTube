package com.example.there.domain.usecase.impl

import com.example.there.domain.model.Subscription
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable
import javax.inject.Inject

class GetUserSubscriptions @Inject constructor(private val repository: IMainRepository) : ObservableUseCase<List<Subscription>, String> {
    override fun execute(params: String?): Observable<List<Subscription>> = repository.getSubs(accessToken = params!!)
}