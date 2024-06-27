package com.example.worldnewsinfo.presentation.mapper

import com.example.worldnewsinfo.domain.NewsParametersDomainModel
import com.example.worldnewsinfo.presentation.filter.FilterState

class NewsParametersDomainModelMapper {
    fun mapNewsParameters(
        filter: FilterState,
        query: String = "1",
        page: Int? = null
    ): NewsParametersDomainModel {
        val str = filter.filterSortList.filter { it.state }.joinToString(
            separator = ",",
            transform = {it.name}
        )
        return NewsParametersDomainModel(
            query = query,
            from = filter.date?.first?.toString(),
            to = filter.date?.second?.toString(),
            language = filter.filterLanguage?.short,
            sortBy = str.ifBlank { null },
            page = page
        )
    }
}