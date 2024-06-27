package com.example.worldnewsinfo.presentation.mapper

import com.example.worldnewsinfo.domain.NewsDomainModel
import com.example.worldnewsinfo.presentation.model.NewsModel

class NewsDomainModelMapper {
    fun mapNewsModel(newsModel: NewsModel): NewsDomainModel{
        return NewsDomainModel(
            0,
            newsModel.newsImg,
            newsModel.sourceName,
            newsModel.shortNewsText,
            newsModel.text,
            newsModel.publishedAt,
            newsModel.newsUrl,
            newsModel.description
        )
    }
}