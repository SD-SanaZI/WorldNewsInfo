package com.example.worldnewsinfo.domain


data class NewsDomainModel(
    val id: Long,
    val newsImg: String?,
    val sourceName: String,
    val shortNewsText: String,
    val text: String?,
    val publishedAt: String,
    val newsUrl: String,
    val description: String?
)

