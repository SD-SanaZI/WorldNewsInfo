package com.example.worldnewsinfo.data.mapper

import com.example.worldnewsinfo.data.room.cache.HeadlinesFilter
import com.example.worldnewsinfo.domain.HeadlinesParametersDomainModel

class HeadlinesFilterMapper {
    fun mapHeadlinesParametersDomainModel(headlinesParametersDomainModel: HeadlinesParametersDomainModel):HeadlinesFilter{
        return HeadlinesFilter(
            0,
            headlinesParametersDomainModel.country,
            headlinesParametersDomainModel.category,
            headlinesParametersDomainModel.pageSize,
            headlinesParametersDomainModel.page
        )
    }
}