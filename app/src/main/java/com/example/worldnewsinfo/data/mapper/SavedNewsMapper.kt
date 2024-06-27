package com.example.worldnewsinfo.data.mapper

import com.example.worldnewsinfo.data.room.save.SavedNews
import com.example.worldnewsinfo.domain.NewsDomainModel

class SavedNewsMapper {
    fun mapNewsDomainModel(newsDomainModel: NewsDomainModel): SavedNews{
        return SavedNews(
            0,
            newsDomainModel.newsImg,
            newsDomainModel.sourceName,
            newsDomainModel.shortNewsText,
            newsDomainModel.text,
            newsDomainModel.publishedAt,
            newsDomainModel.newsUrl,
            System.currentTimeMillis(),
            newsDomainModel.description
        )
    }
}