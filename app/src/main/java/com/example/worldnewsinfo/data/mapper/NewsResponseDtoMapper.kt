package com.example.worldnewsinfo.data.mapper

import com.example.worldnewsinfo.data.newtworkModule.Article
import com.example.worldnewsinfo.data.model.NewsResponseDto
import com.example.worldnewsinfo.data.room.cache.News

class NewsResponseDtoMapper {
    fun mapArticle(article: Article): NewsResponseDto {
        return NewsResponseDto(
            article.urlToImage,
            article.source.name,
            article.title,
            article.content,
            article.publishedAt,
            article.url,
            article.description
        )
    }

    fun mapNews(news: News): NewsResponseDto {
        return NewsResponseDto(
            news.imgUrl,
            news.sourceName,
            news.shortNewsText,
            news.text,
            news.publishedAt,
            news.newsUrl,
            news.description
        )
    }
}