package com.example.worldnewsinfo.presentation.model

import androidx.annotation.DrawableRes
import java.io.Serializable

data class NewsModel(
    val id: Long,
    val newsImg: String?,
    @DrawableRes val sourceImg: Int,
    val sourceName: String,
    val shortNewsText: String,
    val text: String?,
    val publishedAt: String,
    val newsUrl: String,
    val description: String?
) : Serializable