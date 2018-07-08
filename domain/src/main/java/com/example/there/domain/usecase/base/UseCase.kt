package com.example.there.domain.usecase.base

interface UseCase<in Params, out Ret> {
    fun execute(params: Params? = null): Ret
}