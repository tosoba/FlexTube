package com.example.there.remote

import com.example.there.remote.model.SubscriptionsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YoutubeService {
    @GET("subscriptions")
    fun getUserSubscriptions(
            @Header("Authorization") authorization: String,
            @Query("pageToken") pageToken: String? = null,
            @Query("part") part: String = "snippet",
            @Query("mine") mine: Boolean = true,
            @Query("maxResults") maxResults: Int = MAX_MAX_RESULTS,
            @Query("order") order: String = DEFAULT_ORDER
    ): Single<SubscriptionsResponse>

    companion object {
        private const val MAX_MAX_RESULTS = 50
        private const val DEFAULT_ORDER = "alphabetical"
    }
}