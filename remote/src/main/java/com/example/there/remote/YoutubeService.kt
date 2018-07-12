package com.example.there.remote

import com.example.there.remote.model.ActivityResponse
import com.example.there.remote.model.ChannelsPlaylistIdResponse
import com.example.there.remote.model.PlaylistItemsResponse
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
            @Query("maxResults") maxResults: Int = MAX_RESULTS,
            @Query("order") order: String = DEFAULT_ORDER
    ): Single<SubscriptionsResponse>

    @GET("channels")
    fun getChannelsPlaylistId(
            @Query("id") ids: String,
            @Query("part") part: String = "contentDetails",
            @Query("maxResults") maxResults: Int = MAX_RESULTS,
            @Query("key") key: String = Keys.YOUTUBE
    ): Single<ChannelsPlaylistIdResponse>

    @GET("playlistItems")
    fun getPlaylistItems(
            @Query("playlistId") id: String,
            @Query("pageToken") pageToken: String? = null,
            @Query("part") part: String = "snippet,contentDetails",
            @Query("maxResults") maxResults: Int = MAX_RESULTS,
            @Query("key") key: String = Keys.YOUTUBE
    ): Single<PlaylistItemsResponse>

    @GET("activities")
    fun getActivities(
            @Header("Authorization") authorization: String,
            @Query("pageToken") pageToken: String? = null,
            @Query("home") home: Boolean = true,
            @Query("part") part: String = "snippet,contentDetails",
            @Query("maxResults") maxResults: Int = MAX_RESULTS
    ): Single<ActivityResponse>

    companion object {
        private const val MAX_RESULTS = 50
        private const val DEFAULT_ORDER = "alphabetical"
    }
}