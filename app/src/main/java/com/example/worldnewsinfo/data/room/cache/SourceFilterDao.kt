package com.example.worldnewsinfo.data.room.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
interface SourceFilterDao {
    @Query("SELECT * FROM sourceentity")
    fun getAllSource(): List<SourceEntity>

    @Query("SELECT * FROM SourceFilter")
    fun getAllFilters(): List<SourceFilter>

    @Transaction
    @Query("SELECT * FROM sourceentity")
    fun getAllSourceWithFilter(): List<NewsWithSource>

    @Transaction
    @Query("SELECT * FROM sourcefilter")
    fun getAllFilterWithSource(): List<SourceWithNews>

    @Transaction
    @Query("SELECT * FROM sourcefilter WHERE country IS :country " +
            "AND category IS :category " +
            "AND language IS :language "
    )
    fun getFilterWithSource(
        country: String? = null,
        category: String?  = null,
        language: String? = null,
    ): List<SourceWithNews>

    @Upsert
    fun insertSource(source: SourceEntity): Long

    @Upsert
    fun insertSourceFilter(sourceFilter: SourceFilter): Long

    @Transaction
    fun upsertSourceFilterWithoutId(sourceFilter: SourceFilter): Long {
        return getAllFilters().find {
            it.copy(sourceFilterId = 0) == sourceFilter.copy(sourceFilterId = 0)
        }?.sourceFilterId ?: insertSourceFilter(sourceFilter)
    }

    @Upsert
    fun insertSourceFilterNewsCrossRef(sourceFilterNewsCrossRef: SourceFilterNewsCrossRef)

    @Delete
    fun deleteSource(source: SourceEntity)

    @Delete
    fun deleteSourceFilter(sourceFilter: SourceFilter)

    @Delete
    fun deleteSourceFilterNewsCrossRef(sourceFilterNewsCrossRef: SourceFilterNewsCrossRef)
}