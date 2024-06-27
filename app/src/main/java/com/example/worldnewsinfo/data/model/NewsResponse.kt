package com.example.worldnewsinfo.data.model

data class NewsResponse(
    val id: Long,
    val newsResponseDto: NewsResponseDto,
)

data class NewsResponseDto(
    val newsImg: String?,
    val sourceName: String,
    val shortNewsText: String,
    val text: String?,
    val publishedAt: String,
    val newsUrl: String,
    val description: String?
)