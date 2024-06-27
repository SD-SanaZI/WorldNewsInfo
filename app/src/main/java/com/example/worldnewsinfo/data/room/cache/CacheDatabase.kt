package com.example.worldnewsinfo.data.room.cache

import androidx.room.Database
import androidx.room.RoomDatabase

const val CACHE_DATABASE_NAME = "CACHE_DATABASE_NAME"


@Database(entities = [Filter::class, News::class, FilterNewsCrossRef::class,
                     HeadlinesFilter::class, HeadlinesNewsCrossRef::class,
                     SourceFilter::class, SourceEntity::class, SourceFilterNewsCrossRef::class]
    , version = 1)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun FilterDao(): FilterDao
    abstract fun HeadlinesFilterDao(): HeadlinesFilterDao
    abstract fun SourceFilterDao(): SourceFilterDao
}