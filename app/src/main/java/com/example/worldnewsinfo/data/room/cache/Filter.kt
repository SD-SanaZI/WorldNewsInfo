package com.example.worldnewsinfo.data.room.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Filter(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "filter_id") val filterId: Long,
    @ColumnInfo(name = "query") val query: String? = null,
    @ColumnInfo(name = "sort_list") val filterSortList: String? = null,
    @ColumnInfo(name = "date_from") val dateFrom: String? = null,
    @ColumnInfo(name = "date_to") val dateTo: String? = null,
    @ColumnInfo(name = "language") val language: String?,
    @ColumnInfo(name = "sources") val sources: String? = null,
    @ColumnInfo(name = "page_size") val pageSize: Int? = null,
    @ColumnInfo(name = "page") val page: Int? = null
)