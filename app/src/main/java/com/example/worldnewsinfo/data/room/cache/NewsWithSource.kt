package com.example.worldnewsinfo.data.room.cache

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class NewsWithSource(
    @Embedded val source: SourceEntity,
    @Relation(
        parentColumn = "source_id",
        entityColumn = "source_filter_id",
        associateBy = Junction(SourceFilterNewsCrossRef::class)
    )
    val sourceFilter: List<SourceFilter>
)