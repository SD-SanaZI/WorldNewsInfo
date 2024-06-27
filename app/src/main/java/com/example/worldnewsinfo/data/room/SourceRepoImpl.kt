package com.example.worldnewsinfo.data.room

import android.content.Context
import androidx.room.Room
import com.example.worldnewsinfo.data.room.cache.CACHE_DATABASE_NAME
import com.example.worldnewsinfo.data.room.cache.CacheDatabase
import com.example.worldnewsinfo.data.room.cache.SourceEntity
import com.example.worldnewsinfo.data.room.cache.SourceFilter
import com.example.worldnewsinfo.data.room.cache.SourceFilterNewsCrossRef
import com.example.worldnewsinfo.data.room.cache.SourceWithNews
import com.example.worldnewsinfo.domain.SourceDomainModel
import com.example.worldnewsinfo.domain.SourceParametersDomainModel

class SourceRepoImpl(context: Context): NewsDataBaseRepository<SourceWithNews, SourceParametersDomainModel, SourceDomainModel> {
    private val dao =  Room.databaseBuilder(
        context,
        CacheDatabase::class.java, CACHE_DATABASE_NAME
    ).build().SourceFilterDao()

    override fun getNews(parameters: SourceParametersDomainModel): List<SourceWithNews> {
        return dao.getFilterWithSource(
            parameters.country,
            parameters.category,
            parameters.language
        )
    }

    override fun saveNews(list: List<SourceDomainModel>?, parameters: SourceParametersDomainModel) {
        val newFilter = SourceFilter(
            0,
            parameters.country,
            parameters.category,
            parameters.language
        )
        val filterId = dao.upsertSourceFilterWithoutId(newFilter)
        list?.forEach {
            val sourceId = dao.insertSource(
                SourceEntity(
                    it.id,
                    it.sourceName,
                    it.supportingText
                )
            )
            dao.insertSourceFilterNewsCrossRef(
                SourceFilterNewsCrossRef(
                    filterId,
                    sourceId
                )
            )
        }
    }

}