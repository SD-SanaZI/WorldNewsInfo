package com.example.worldnewsinfo.domain

class LoadNewsFromEverythingUseCase (
    private val repository: Repository,
    private val newsParametersDomainModel: NewsParametersDomainModel
) {
    suspend operator fun invoke(): List<NewsDomainModel>{
        return repository.loadNewsFromEverything(newsParametersDomainModel)
    }
}