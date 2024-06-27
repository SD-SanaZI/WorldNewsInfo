package com.example.worldnewsinfo.data.newtworkModule

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class NetworkModule {
    private val baseUrl: String = "https://newsapi.org/v2/"

    val mainService: NewsApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor()).build())
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .create()
            )
        )
        .build()
        .create()
}

