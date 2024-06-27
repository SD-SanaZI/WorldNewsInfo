package com.example.worldnewsinfo.data.room.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
interface FilterDao {
    @Query("SELECT * FROM news")
    fun getAllNews(): List<News>

    @Query("SELECT * FROM Filter")
    fun getAllFilters(): List<Filter>

    @Transaction
    @Query("SELECT * FROM news")
    fun getAllNewsWithFilter(): List<NewsWithFilter>

    @Transaction
    @Query("SELECT * FROM filter")
    fun getAllFilterWithNews(): List<FilterWithNews>

    @Transaction
    @Query("SELECT * FROM filter WHERE `query` IS :query " +
            "AND sort_list IS :filterSortList " +
            "AND date_from IS :dateFrom " +
            "AND date_to IS :dateTo " +
            "AND language IS :language " +
            "AND sources IS :sources " +
            "AND page_size IS :pageSize " +
            "AND page IS :page ")
    fun getFilterWithNews(
            query: String? = null,
            filterSortList: String? = null,
            dateFrom: String? = null,
            dateTo: String? = null,
            language: String?,
            sources: String? = null,
            pageSize: Int? = null,
            page: Int? = null
    ): List<FilterWithNews>

    @Upsert
    fun insertNews(news: News): Long

    @Transaction
    fun upsertNewsWithoutId(news: News): Long{
        return getAllNews().find {
            it.copy(newsId = 0) == news.copy(newsId = 0)
        }?.newsId ?: insertNews(news)
    }

    @Upsert
    fun insertFilter(filter: Filter): Long

    @Transaction
    fun upsertFilterWithoutId(filter: Filter): Long {
        return getAllFilters().find {
            it.copy(filterId = 0) == filter.copy(filterId = 0)
        }?.filterId ?: insertFilter(filter)
    }

    @Upsert
    fun insertFilterNewsCrossRef(filterNewsCrossRef: FilterNewsCrossRef)

    @Delete
    fun deleteNews(news: News)

    @Delete
    fun deleteFilter(filter: Filter)

    @Delete
    fun deleteFilterNewsCrossRef(filterNewsCrossRef: FilterNewsCrossRef)
}