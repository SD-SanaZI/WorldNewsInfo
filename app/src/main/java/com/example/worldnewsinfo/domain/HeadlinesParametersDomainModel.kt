package com.example.worldnewsinfo.domain

data class HeadlinesParametersDomainModel(
    val country: String? = null,
    val category: String = "",
    val pageSize: Int? = null,
    val page: Int? = null
)