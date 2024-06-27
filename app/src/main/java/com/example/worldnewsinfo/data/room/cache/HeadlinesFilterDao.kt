package com.example.worldnewsinfo.data.room.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
interface HeadlinesFilterDao {
    @Query("SELECT * FROM news")
    fun getAllNews(): List<News>

    @Query("SELECT * FROM headlinesfilter")
    fun getAllHeadlines(): List<HeadlinesFilter>

    @Transaction
    @Query("SELECT * FROM news")
    fun getAllNewsWithHeadlines(): List<NewsWithHeadlines>

    @Transaction
    @Query("SELECT * FROM headlinesfilter")
    fun getAllHeadlinesWithNews(): List<HeadlinesWithNews>

    @Transaction
    @Query("SELECT * FROM headlinesfilter WHERE country IS :country " +
            "AND category IS :category " +
            "AND page_size IS :pageSize " +
            "AND page IS :page ")
    fun getHeadlinesWithNews(
        country: String? = null,
        category: String,
        pageSize: Int? = null,
        page: Int? = null
    ): List<HeadlinesWithNews>

    @Upsert
    fun insertNews(news: News): Long

    @Transaction
    fun upsertNewsWithoutId(news: News): Long{
        return getAllNews().find {
            it.copy(newsId = 0) == news.copy(newsId = 0)
        }?.newsId ?: insertNews(news)
    }

    @Upsert
    fun insertHeadlinesFilter(headlinesFilter: HeadlinesFilter): Long

    @Transaction
    fun upsertHeadlinesFilterWithoutId(headlinesFilter: HeadlinesFilter): Long {
        return getAllHeadlines().find<HeadlinesFilter> {
            it.copy(headlinesFilterId = 0) == headlinesFilter.copy(headlinesFilterId = 0)
        }?.headlinesFilterId ?: insertHeadlinesFilter(headlinesFilter)
    }

    @Upsert
    fun insertHeadlinesNewsCrossRef(headlinesNewsCrossRef: HeadlinesNewsCrossRef)

    @Delete
    fun deleteNews(news: News)

    @Delete
    fun deleteHeadlinesFilter(headlinesFilter: HeadlinesFilter)

    @Delete
    fun deleteHeadlinesNewsCrossRef(headlinesNewsCrossRef: HeadlinesNewsCrossRef)
}