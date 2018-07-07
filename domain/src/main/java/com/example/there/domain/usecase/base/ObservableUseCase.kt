package com.example.there.domain.usecase.base

import io.reactivex.Observable

interface ObservableUseCase<T, in Params> {
    fun execute(params: Params? = null): Observable<T>
}