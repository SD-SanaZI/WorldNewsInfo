package com.example.worldnewsinfo.data

import com.example.worldnewsinfo.data.mapper.NewsDomainModelMapper
import com.example.worldnewsinfo.data.mapper.NewsResponseDtoMapper
import com.example.worldnewsinfo.data.model.NewsResponseDto
import com.example.worldnewsinfo.data.model.NewsResponse
import com.example.worldnewsinfo.data.newtworkModule.NetworkModule
import com.example.worldnewsinfo.data.newtworkModule.ResponseNews
import com.example.worldnewsinfo.data.newtworkModule.ResponseSource
import com.example.worldnewsinfo.data.room.NewsDataBaseRepository
import com.example.worldnewsinfo.data.room.SaveDataBaseImpl
import com.example.worldnewsinfo.data.room.cache.FilterWithNews
import com.example.worldnewsinfo.data.room.cache.HeadlinesWithNews
import com.example.worldnewsinfo.data.room.cache.SourceWithNews
import com.example.worldnewsinfo.domain.HeadlinesParametersDomainModel
import com.example.worldnewsinfo.domain.NewsDomainModel
import com.example.worldnewsinfo.domain.NewsParametersDomainModel
import com.example.worldnewsinfo.domain.Repository
import com.example.worldnewsinfo.domain.SourceDomainModel
import com.example.worldnewsinfo.domain.SourceParametersDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val TWO_WEEKS_IN_MILLIS = 1000L * 60 * 60 * 24 * 7 * 2
const val STANDARD_PAGE_SIZE: Int = 20

class RepositoryImpl(
    private val headlinesNewsRepo: NewsDataBaseRepository<HeadlinesWithNews, HeadlinesParametersDomainModel, NewsResponseDto>,
    private val everythingNewsRepo: NewsDataBaseRepository<FilterWithNews, NewsParametersDomainModel, NewsResponseDto>,
    private val sourceRepo: NewsDataBaseRepository<SourceWithNews, SourceParametersDomainModel, SourceDomainModel>,
    private val saveDataBaseImpl: SaveDataBaseImpl
) : Repository {
    override suspend fun loadNewsFromHeadlines(headlinesParametersDomainModel: HeadlinesParametersDomainModel): List<NewsDomainModel> {
        val data = getNetNewsFromHeadlines(headlinesParametersDomainModel).also {
            saveCacheNewsFromHeadlines(it, headlinesParametersDomainModel)
        } ?: getCacheNewsFromHeadlines(headlinesParametersDomainModel) ?: emptyList()
        return newsDomainModelListFromNewsModelDtoList(data)
    }

    private suspend fun getNetNewsFromHeadlines(headlinesParametersDomainModel: HeadlinesParametersDomainModel): List<NewsResponseDto>? {
        return runCatching {
            val net = NetworkModule().mainService
            val responseNews: ResponseNews = net.getTopHeadlinesNews(
                country = headlinesParametersDomainModel.country,
                category = headlinesParametersDomainModel.category,
                pageSize = headlinesParametersDomainModel.pageSize,
                page = headlinesParametersDomainModel.page
            )
            val list = responseNews.articles.map {
                NewsResponseDtoMapper().mapArticle(it)
            }
            return list.ifEmpty { null }
        }.getOrNull()
    }

    private suspend fun saveCacheNewsFromHeadlines(
        list: List<NewsResponseDto>?,
        headlinesParametersDomainModel: HeadlinesParametersDomainModel
    ) {
        withContext(Dispatchers.IO) {
            headlinesNewsRepo.saveNews(list, headlinesParametersDomainModel)
        }
    }

    private suspend fun getCacheNewsFromHeadlines(headlinesParametersDomainModel: HeadlinesParametersDomainModel): List<NewsResponseDto>? {
        return withContext(Dispatchers.IO) {
            val filterWithNews = headlinesNewsRepo.getNews(headlinesParametersDomainModel)
            return@withContext filterWithNews.firstOrNull()?.news?.map {
                NewsResponseDtoMapper().mapNews(it)
            }
        }
    }

    override suspend fun loadNewsFromEverything(newsParametersDomainModel: NewsParametersDomainModel): List<NewsDomainModel> {
        val data = getNetNewsFromEverything(newsParametersDomainModel).also {
            saveCacheNewsFromEverything(it, newsParametersDomainModel)
        } ?: getCacheNewsFromEverything(newsParametersDomainModel) ?: emptyList()
        return newsDomainModelListFromNewsModelDtoList(data)
    }

    private suspend fun getNetNewsFromEverything(newsParametersDomainModel: NewsParametersDomainModel): List<NewsResponseDto>? {
        return runCatching {
            val net = NetworkModule().mainService
            val responseNews: ResponseNews = net.getEverythingNews(
                query = newsParametersDomainModel.query,
                from = newsParametersDomainModel.from,
                to = newsParametersDomainModel.to,
                language = newsParametersDomainModel.language,
                sources = newsParametersDomainModel.sources,
                sortBy = newsParametersDomainModel.sortBy,
                pageSize = newsParametersDomainModel.pageSize,
                page = newsParametersDomainModel.page
            )
            val list = responseNews.articles.map {
                NewsResponseDtoMapper().mapArticle(it)
            }
            return list.take(newsParametersDomainModel.page ?: STANDARD_PAGE_SIZE).ifEmpty { null }
        }.getOrNull()
    }

    private suspend fun saveCacheNewsFromEverything(
        list: List<NewsResponseDto>?,
        newsParametersDomainModel: NewsParametersDomainModel
    ) {
        withContext(Dispatchers.IO) {
            everythingNewsRepo.saveNews(list,newsParametersDomainModel)
        }
    }

    private suspend fun getCacheNewsFromEverything(newsParametersDomainModel: NewsParametersDomainModel): List<NewsResponseDto>? {
        return withContext(Dispatchers.IO) {
            val filterWithNews = everythingNewsRepo.getNews(newsParametersDomainModel)
            return@withContext filterWithNews.firstOrNull()?.news?.map {
                NewsResponseDtoMapper().mapNews(it)
            }
        }
    }

    private fun newsDomainModelListFromNewsModelDtoList(data: List<NewsResponseDto>): List<NewsDomainModel> {
        val newsResponseList = data.mapIndexed { index, newsModelDto ->
            NewsResponse(index.toLong(), newsModelDto)
        }
        return newsResponseList.map { NewsDomainModelMapper().mapNewsResponse(it) }
    }

    override suspend fun loadSourceList(sourceParametersDomainModel: SourceParametersDomainModel): List<SourceDomainModel> {
        return getNetSourceData(sourceParametersDomainModel).also {
            saveCacheSource(it, sourceParametersDomainModel)
        } ?: getCacheSource(sourceParametersDomainModel) ?: listOf(
            SourceDomainModel("", "Empty Source List", "General | USA"),
        )
    }

    private suspend fun getNetSourceData(sourceParametersDomainModel: SourceParametersDomainModel): List<SourceDomainModel>? {
        return runCatching {
            val net = NetworkModule().mainService
            val responseSource: ResponseSource = net.getSources(
                language = sourceParametersDomainModel.language,
                category = sourceParametersDomainModel.category,
                country = sourceParametersDomainModel.country
            )
            val list = responseSource.sources.map {
                SourceDomainModel(
                    it.id,
                    it.name,
                    "${it.category} | ${it.country}"
                )
            }
            list.ifEmpty { null }
        }.getOrNull()
    }

    private suspend fun saveCacheSource(
        list: List<SourceDomainModel>?,
        sourceParametersDomainModel: SourceParametersDomainModel
    ) {
        withContext(Dispatchers.IO) {
            sourceRepo.saveNews(list,sourceParametersDomainModel)
        }
    }

    private suspend fun getCacheSource(sourceParametersDomainModel: SourceParametersDomainModel): List<SourceDomainModel>? {
        return withContext(Dispatchers.IO) {
            val filterWithSource = sourceRepo.getNews(sourceParametersDomainModel)
            return@withContext filterWithSource.firstOrNull()?.source?.map {
                SourceDomainModel(
                    it.sourceId,
                    it.sourceName,
                    it.supportingText
                )
            }
        }
    }

    override suspend fun saveNews(newsDomainModel: NewsDomainModel) {
        withContext(Dispatchers.IO) {
            saveDataBaseImpl.saveNews(newsDomainModel)
        }
    }

    override suspend fun areNewsSaved(newsDomainModel: NewsDomainModel): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext saveDataBaseImpl.areNewsSaved(newsDomainModel)
        }
    }

    override suspend fun deleteNews(newsDomainModel: NewsDomainModel) {
        withContext(Dispatchers.IO) {
            saveDataBaseImpl.deleteNews(newsDomainModel)
        }
    }

    override suspend fun deleteOldNews(date: Long) {
        withContext(Dispatchers.IO) {
            saveDataBaseImpl.deleteOldNews(date)
        }
    }

    override suspend fun savedNews(): List<NewsDomainModel> {
        return withContext(Dispatchers.IO) {
            return@withContext saveDataBaseImpl.savedNews()
        }
    }
}