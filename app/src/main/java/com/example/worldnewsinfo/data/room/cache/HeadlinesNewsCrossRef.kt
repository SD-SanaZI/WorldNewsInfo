package com.example.worldnewsinfo.data.room.cache

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["headlines_filter_id", "news_id"])
data class HeadlinesNewsCrossRef(
    @ColumnInfo(name = "headlines_filter_id") val headlinesFilterId: Long,
    @ColumnInfo(name = "news_id") val newsId: Long
)