package com.example.worldnewsinfo.data.room.cache

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SourceWithNews(
    @Embedded val sourceFilter: SourceFilter,
    @Relation(
        parentColumn = "source_filter_id",
        entityColumn = "source_id",
        associateBy = Junction(SourceFilterNewsCrossRef::class)
    )
    val source: List<SourceEntity>
)