package com.example.worldnewsinfo.data.room.save

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SaveNewsDao {
    @Query("SELECT * FROM savednews")
    fun getAllNews(): List<SavedNews>

    @Transaction
    @Query(
        "SELECT * FROM savednews WHERE img_url IS :imgUrl " +
                "AND source_name IS :sourceName " +
                "AND short_news_text IS :shortNewsText " +
                "AND text IS :text " +
                "AND published_at IS :publishedAt " +
                "AND news_url IS :newsUrl " +
                "AND description IS :description "
    )
    fun getNews(
        imgUrl: String?,
        sourceName: String,
        shortNewsText: String,
        text: String?,
        publishedAt: String,
        newsUrl: String,
        description: String?
    ): List<SavedNews>

    @Query("DELETE FROM savednews WHERE save_at < :date")
    fun deleteLater(date: Long)

    @Insert
    fun insertNews(news: SavedNews): Long

    @Delete
    fun deleteNews(news: SavedNews)

    @Delete(entity = SavedNews::class)
    fun deleteNews(news: SavedNewsWithoutId)
}