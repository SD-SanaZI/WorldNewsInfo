package com.example.worldnewsinfo.data.mapper

import com.example.worldnewsinfo.data.model.NewsResponse
import com.example.worldnewsinfo.data.room.save.SavedNews
import com.example.worldnewsinfo.domain.NewsDomainModel

class NewsDomainModelMapper {
    fun mapNewsResponse(response: NewsResponse): NewsDomainModel {
        return NewsDomainModel(
            response.id,
            response.newsResponseDto.newsImg,
            response.newsResponseDto.sourceName,
            response.newsResponseDto.shortNewsText,
            response.newsResponseDto.text,
            response.newsResponseDto.publishedAt,
            response.newsResponseDto.newsUrl,
            response.newsResponseDto.description
        )
    }

    fun mapSavedNews(savedNews: SavedNews): NewsDomainModel {
        return NewsDomainModel(
            savedNews.newsId,
            savedNews.imgUrl,
            savedNews.sourceName,
            savedNews.shortNewsText,
            savedNews.text,
            savedNews.publishedAt,
            savedNews.newsUrl,
            savedNews.description
        )
    }
}