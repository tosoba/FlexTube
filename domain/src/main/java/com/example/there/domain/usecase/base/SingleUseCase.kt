package com.example.there.domain.usecase.base

import io.reactivex.Single

interface SingleUseCase<T, in Params> {
    fun execute(params: Params? = null): Single<T>
}