package com.example.there.domain.usecase.base

import io.reactivex.Scheduler
import io.reactivex.Single

abstract class SingleUseCase<T, in Params>(
        private val threadScheduler: Scheduler,
        private val postExecutionScheduler: Scheduler) {

    /**
     * Builds a [Single] which will be used when the current [SingleUseCase] is executed.
     */
    protected abstract fun buildUseCaseObservable(params: Params? = null): Single<T>

    /**
     * Executes the current use case.
     */
    open fun execute(params: Params? = null): Single<T> = buildUseCaseObservable(params)
            .subscribeOn(threadScheduler)
            .observeOn(postExecutionScheduler)

}