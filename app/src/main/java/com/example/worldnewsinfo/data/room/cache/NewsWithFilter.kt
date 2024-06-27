package com.example.worldnewsinfo.data.room.cache

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class NewsWithFilter(
    @Embedded val news: News,
    @Relation(
        parentColumn = "news_id",
        entityColumn = "filter_id",
        associateBy = Junction(FilterNewsCrossRef::class)
    )
    val filter: List<Filter>
)