package com.example.worldnewsinfo.data.mapper

import com.example.worldnewsinfo.data.model.NewsResponseDto
import com.example.worldnewsinfo.data.room.cache.News

class NewsMapper {
    fun mapNewsModelDto(newsResponseDto: NewsResponseDto): News{
        return News(
            0,
            newsResponseDto.newsImg,
            newsResponseDto.sourceName,
            newsResponseDto.shortNewsText,
            newsResponseDto.text,
            newsResponseDto.publishedAt,
            newsResponseDto.newsUrl,
            newsResponseDto.description
        )
    }
}