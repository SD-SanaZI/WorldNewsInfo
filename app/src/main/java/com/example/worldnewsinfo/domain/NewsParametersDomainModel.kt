package com.example.worldnewsinfo.domain

data class NewsParametersDomainModel(
    val query: String? = null,
    val from: String? = null,
    val to: String? = null,
    val language: String? = null,
    val sources: String? = null,
    val sortBy: String? = null,
    val pageSize: Int? = null,
    val page: Int? = null
)