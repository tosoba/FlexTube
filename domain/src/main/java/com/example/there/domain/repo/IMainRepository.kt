package com.example.there.domain.repo

import com.example.there.domain.model.PlaylistItem
import com.example.there.domain.model.Subscription
import io.reactivex.Observable

interface IMainRepository {
    fun getSubs(accessToken: String): Observable<List<Subscription>>

    fun getVideos(channelIds: List<String>): Observable<List<PlaylistItem>>
}