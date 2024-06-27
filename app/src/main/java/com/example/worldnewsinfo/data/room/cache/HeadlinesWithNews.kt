package com.example.worldnewsinfo.data.room.cache

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class HeadlinesWithNews(
    @Embedded val headlinesFilter: HeadlinesFilter,
    @Relation(
        parentColumn = "headlines_filter_id",
        entityColumn = "news_id",
        associateBy = Junction(HeadlinesNewsCrossRef::class)
    )
    val news: List<News>
)