package com.example.worldnewsinfo.data.newtworkModule

data class ResponseNews(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)