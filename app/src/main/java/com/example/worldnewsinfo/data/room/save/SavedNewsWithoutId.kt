package com.example.worldnewsinfo.data.room.save

import androidx.room.ColumnInfo

data class SavedNewsWithoutId(
    @ColumnInfo(name = "img_url") val imgUrl: String? = null,
    @ColumnInfo(name = "source_name") val sourceName: String,
    @ColumnInfo(name = "short_news_text") val shortNewsText: String,
    @ColumnInfo(name = "text") val text: String? = null,
    @ColumnInfo(name = "published_at") val publishedAt: String,
    @ColumnInfo(name = "news_url") val newsUrl: String,
    @ColumnInfo(name = "description") val description: String?
)