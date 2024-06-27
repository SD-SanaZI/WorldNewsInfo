package com.example.worldnewsinfo.domain

class LoadSourcesUseCase (
    private val repository: Repository,
    private val sourceParametersDomainModel: SourceParametersDomainModel) {
    suspend operator fun invoke() = repository.loadSourceList(sourceParametersDomainModel)
}