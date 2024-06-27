package com.example.worldnewsinfo.data.room

import android.content.Context
import androidx.room.Room
import com.example.worldnewsinfo.data.mapper.FilterMapper
import com.example.worldnewsinfo.data.mapper.NewsMapper
import com.example.worldnewsinfo.data.model.NewsResponseDto
import com.example.worldnewsinfo.data.room.cache.CACHE_DATABASE_NAME
import com.example.worldnewsinfo.data.room.cache.CacheDatabase
import com.example.worldnewsinfo.data.room.cache.FilterNewsCrossRef
import com.example.worldnewsinfo.data.room.cache.FilterWithNews
import com.example.worldnewsinfo.domain.NewsParametersDomainModel

class EverythingNewsRepoImpl(context: Context): NewsDataBaseRepository<FilterWithNews, NewsParametersDomainModel, NewsResponseDto> {
    private val dao =  Room.databaseBuilder(
        context,
        CacheDatabase::class.java, CACHE_DATABASE_NAME
    ).build().FilterDao()

    override fun getNews(parameters: NewsParametersDomainModel): List<FilterWithNews> {
        return dao.getFilterWithNews(
            parameters.query,
            parameters.sortBy,
            parameters.from,
            parameters.to,
            parameters.language,
            parameters.sources,
            parameters.pageSize,
            parameters.page
        )
    }

    override fun saveNews(
        list: List<NewsResponseDto>?,
        parameters: NewsParametersDomainModel
    ) {
        val newFilter = FilterMapper().mapNewsParametersDomainModel(parameters)
        val filterId = dao.upsertFilterWithoutId(newFilter)
        list?.forEach {
            val newsId = dao.upsertNewsWithoutId(
                NewsMapper().mapNewsModelDto(it)
            )
            dao.insertFilterNewsCrossRef(FilterNewsCrossRef(filterId, newsId))
        }
    }

}