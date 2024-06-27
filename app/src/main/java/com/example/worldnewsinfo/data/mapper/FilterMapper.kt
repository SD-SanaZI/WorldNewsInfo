package com.example.worldnewsinfo.data.mapper

import com.example.worldnewsinfo.data.room.cache.Filter
import com.example.worldnewsinfo.domain.NewsParametersDomainModel

class FilterMapper {
    fun mapNewsParametersDomainModel(newsParametersDomainModel: NewsParametersDomainModel):Filter{
        return Filter(
            0,
            newsParametersDomainModel.query,
            newsParametersDomainModel.sortBy,
            newsParametersDomainModel.from,
            newsParametersDomainModel.to,
            newsParametersDomainModel.language,
            newsParametersDomainModel.sources,
            newsParametersDomainModel.pageSize,
            newsParametersDomainModel.page
        )
    }
}