package com.example.worldnewsinfo.data.room

import android.content.Context
import androidx.room.Room
import com.example.worldnewsinfo.data.TWO_WEEKS_IN_MILLIS
import com.example.worldnewsinfo.data.mapper.NewsDomainModelMapper
import com.example.worldnewsinfo.data.mapper.SavedNewsMapper
import com.example.worldnewsinfo.data.mapper.SavedNewsWithoutIdMapper
import com.example.worldnewsinfo.data.room.save.SAVE_DATABASE_NAME
import com.example.worldnewsinfo.data.room.save.SaveDatabase
import com.example.worldnewsinfo.domain.NewsDomainModel

class SaveDataBaseImpl(context: Context) {
    val dao = Room.databaseBuilder(
        context,
        SaveDatabase::class.java, SAVE_DATABASE_NAME
    ).build().SaveNewsDao()

    fun saveNews(newsDomainModel: NewsDomainModel) {
        dao.insertNews(
            SavedNewsMapper().mapNewsDomainModel(newsDomainModel)
        )
    }

    fun areNewsSaved(newsDomainModel: NewsDomainModel): Boolean {
        return dao.getNews(
            newsDomainModel.newsImg,
            newsDomainModel.sourceName,
            newsDomainModel.shortNewsText,
            newsDomainModel.text,
            newsDomainModel.publishedAt,
            newsDomainModel.newsUrl,
            newsDomainModel.description
        ).isNotEmpty()
    }

    fun deleteNews(newsDomainModel: NewsDomainModel) {
        dao.deleteNews(
            SavedNewsWithoutIdMapper().mapNewsDomainModel(newsDomainModel)
        )
    }

    fun deleteOldNews(date: Long) {
        dao.deleteLater(date - TWO_WEEKS_IN_MILLIS)
    }

    fun savedNews(): List<NewsDomainModel> {
        return dao.getAllNews().map {
            NewsDomainModelMapper().mapSavedNews(it)
        }
    }
}