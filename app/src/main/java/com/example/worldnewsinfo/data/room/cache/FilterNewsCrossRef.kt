package com.example.worldnewsinfo.data.room.cache

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["filter_id", "news_id"])
data class FilterNewsCrossRef(
    @ColumnInfo(name = "filter_id") val filterId: Long,
    @ColumnInfo(name = "news_id") val newsId: Long
)