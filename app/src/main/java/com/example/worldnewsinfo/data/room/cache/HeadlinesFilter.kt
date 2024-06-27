package com.example.worldnewsinfo.data.room.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HeadlinesFilter(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "headlines_filter_id") val headlinesFilterId: Long,
    @ColumnInfo(name = "country") val country: String? = null,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "page_size") val pageSize: Int? = null,
    @ColumnInfo(name = "page") val page: Int? = null
)