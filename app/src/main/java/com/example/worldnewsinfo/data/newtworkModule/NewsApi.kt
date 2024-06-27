package com.example.worldnewsinfo.data.newtworkModule

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything")
    suspend fun getEverythingNews(
        @Query("q") query: String? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("language") language: String? = null,
        @Query("sources") sources: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("page") page: Int? = null
    ): ResponseNews

    @GET("top-headlines")
    suspend fun getTopHeadlinesNews(
        @Query("country") country: String? = null,
        @Query("category") category: String? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("page") page: Int? = null
    ): ResponseNews

    @GET("top-headlines/sources")
    suspend fun getSources(
        @Query("language") language: String? = null,
        @Query("category") category: String? = null,
        @Query("country") country: String? = null
    ) : ResponseSource
}