package com.example.there.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object YoutubeServiceFactory {

    private const val baseUrl = "https://www.googleapis.com/youtube/v3/"

    fun makeYoutubeService(isDebug: Boolean): YoutubeService = makeYoutubeService(
            okHttpClient = makeOkHttpClient(makeLoggingInterceptor(isDebug)),
            gson = makeGson()
    )

    private fun makeYoutubeService(okHttpClient: OkHttpClient, gson: Gson): YoutubeService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .run { create(YoutubeService::class.java) }

    private fun makeOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()

    private fun makeGson(): Gson = GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()

    private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (isDebug)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }
}
