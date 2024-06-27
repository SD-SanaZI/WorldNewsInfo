package com.example.worldnewsinfo.domain

interface Repository {

    suspend fun loadNewsFromHeadlines(headlinesParametersDomainModel: HeadlinesParametersDomainModel): List<NewsDomainModel>

    suspend fun loadNewsFromEverything(newsParametersDomainModel: NewsParametersDomainModel): List<NewsDomainModel>

    suspend fun loadSourceList(sourceParametersDomainModel: SourceParametersDomainModel):List<SourceDomainModel>

    suspend fun saveNews(newsDomainModel:NewsDomainModel)

    suspend fun savedNews(): List<NewsDomainModel>

    suspend fun areNewsSaved(newsDomainModel:NewsDomainModel): Boolean

    suspend fun deleteNews(newsDomainModel:NewsDomainModel)

    suspend fun deleteOldNews(date: Long)

    // ...
}