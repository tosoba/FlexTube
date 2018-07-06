package com.example.there.domain.usecase.base

import io.reactivex.Observable
import io.reactivex.Scheduler

abstract class ObservableUseCase<T, in Params>(
        private val threadScheduler: Scheduler,
        private val postExecutionScheduler: Scheduler) {

    /**
     * Builds a [Observable] which will be used when the current [ObservableUseCase] is executed.
     */
    protected abstract fun buildUseCaseObservable(params: Params? = null): Observable<T>

    /**
     * Executes the current use case.
     */
    open fun execute(params: Params? = null): Observable<T> = buildUseCaseObservable(params)
            .subscribeOn(threadScheduler)
            .observeOn(postExecutionScheduler)

}