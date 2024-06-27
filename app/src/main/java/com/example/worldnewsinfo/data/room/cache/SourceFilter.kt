package com.example.worldnewsinfo.data.room.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SourceFilter(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "source_filter_id") val sourceFilterId: Long,
    @ColumnInfo(name = "country") val country: String? = null,
    @ColumnInfo(name = "category") val category: String? = null,
    @ColumnInfo(name = "language") val language: String? = null,
)