package com.example.worldnewsinfo.data.room.cache

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class NewsWithHeadlines(
    @Embedded val news: News,
    @Relation(
        parentColumn = "news_id",
        entityColumn = "headlines_filter_id",
        associateBy = Junction(HeadlinesNewsCrossRef::class)
    )
    val headlinesFilter: List<HeadlinesFilter>
)