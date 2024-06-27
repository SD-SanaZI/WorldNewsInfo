package com.example.worldnewsinfo.data.mapper

import com.example.worldnewsinfo.data.room.save.SavedNewsWithoutId
import com.example.worldnewsinfo.domain.NewsDomainModel

class SavedNewsWithoutIdMapper {
    fun mapNewsDomainModel(newsDomainModel: NewsDomainModel): SavedNewsWithoutId {
        return SavedNewsWithoutId(
            newsDomainModel.newsImg,
            newsDomainModel.sourceName,
            newsDomainModel.shortNewsText,
            newsDomainModel.text,
            newsDomainModel.publishedAt,
            newsDomainModel.newsUrl,
            newsDomainModel.description
        )
    }
}