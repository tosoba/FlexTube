package com.example.there.domain.usecase.impl

import com.example.there.domain.model.Subscription
import com.example.there.domain.repo.IMainRepository
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetUserSubscriptions(
        private val repository: IMainRepository,
        threadScheduler: Scheduler,
        postExecutionScheduler: Scheduler
) : ObservableUseCase<List<Subscription>, String>(threadScheduler, postExecutionScheduler) {
    override fun buildUseCaseObservable(params: String?): Observable<List<Subscription>> = repository.getSubs(accessToken = params!!)
}