package com.example.worldnewsinfo.domain

class LoadingNewsUseCase(
    private val repository: Repository,
    private val headlinesParametersDomainModel: HeadlinesParametersDomainModel
) {
    suspend operator fun invoke(): List<NewsDomainModel>{
        return repository.loadNewsFromHeadlines(headlinesParametersDomainModel)
    }
}