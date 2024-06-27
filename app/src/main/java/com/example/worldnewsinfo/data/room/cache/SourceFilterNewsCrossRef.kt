package com.example.worldnewsinfo.data.room.cache

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["source_filter_id", "source_id"])
data class SourceFilterNewsCrossRef(
    @ColumnInfo(name = "source_filter_id") val sourceFilterId: Long,
    @ColumnInfo(name = "source_id") val sourceId: Long
)