package com.example.worldnewsinfo.data.room.cache

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FilterWithNews(
    @Embedded val filter: Filter,
    @Relation(
        parentColumn = "filter_id",
        entityColumn = "news_id",
        associateBy = Junction(FilterNewsCrossRef::class)
    )
    val news: List<News>
)