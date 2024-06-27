package com.example.worldnewsinfo.data.room

import android.content.Context
import androidx.room.Room
import com.example.worldnewsinfo.data.mapper.HeadlinesFilterMapper
import com.example.worldnewsinfo.data.mapper.NewsMapper
import com.example.worldnewsinfo.data.model.NewsResponseDto
import com.example.worldnewsinfo.data.room.cache.CACHE_DATABASE_NAME
import com.example.worldnewsinfo.data.room.cache.CacheDatabase
import com.example.worldnewsinfo.data.room.cache.HeadlinesNewsCrossRef
import com.example.worldnewsinfo.data.room.cache.HeadlinesWithNews
import com.example.worldnewsinfo.domain.HeadlinesParametersDomainModel

class HeadlinesNewsRepoImpl(context: Context): NewsDataBaseRepository<HeadlinesWithNews, HeadlinesParametersDomainModel, NewsResponseDto> {
    private val dao = Room.databaseBuilder(
        context,
        CacheDatabase::class.java, CACHE_DATABASE_NAME
    ).build().HeadlinesFilterDao()

    override fun getNews(parameters: HeadlinesParametersDomainModel): List<HeadlinesWithNews> {
        return dao.getHeadlinesWithNews(
            parameters.country,
            parameters.category,
            parameters.pageSize,
            parameters.page
        )
    }

    override fun saveNews(
        list: List<NewsResponseDto>?,
        parameters: HeadlinesParametersDomainModel
    ) {
        val newFilter = HeadlinesFilterMapper().mapHeadlinesParametersDomainModel(parameters)
        val filterId =  dao.upsertHeadlinesFilterWithoutId(newFilter)
        list?.forEach {
            val newsId = dao.upsertNewsWithoutId(
                NewsMapper().mapNewsModelDto(it)
            )
            dao.insertHeadlinesNewsCrossRef(
                HeadlinesNewsCrossRef(
                    filterId,
                    newsId
                )
            )
        }
    }

}