package com.example.there.domain.repo

import com.example.there.domain.model.Subscription
import io.reactivex.Observable
import io.reactivex.Single

interface IMainRepository {
    fun getSubs(accessToken: String): Observable<List<Subscription>>
}