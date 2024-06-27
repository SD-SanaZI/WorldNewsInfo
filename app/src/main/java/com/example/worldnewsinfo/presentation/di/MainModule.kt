package com.example.worldnewsinfo.presentation.di

import android.content.Context
import com.example.worldnewsinfo.data.RepositoryImpl
import com.example.worldnewsinfo.data.model.NewsResponseDto
import com.example.worldnewsinfo.data.room.EverythingNewsRepoImpl
import com.example.worldnewsinfo.data.room.HeadlinesNewsRepoImpl
import com.example.worldnewsinfo.data.room.NewsDataBaseRepository
import com.example.worldnewsinfo.data.room.SaveDataBaseImpl
import com.example.worldnewsinfo.data.room.SourceRepoImpl
import com.example.worldnewsinfo.data.room.cache.FilterWithNews
import com.example.worldnewsinfo.data.room.cache.HeadlinesWithNews
import com.example.worldnewsinfo.data.room.cache.SourceWithNews
import com.example.worldnewsinfo.domain.HeadlinesParametersDomainModel
import com.example.worldnewsinfo.domain.NewsParametersDomainModel
import com.example.worldnewsinfo.domain.Repository
import com.example.worldnewsinfo.domain.SourceDomainModel
import com.example.worldnewsinfo.domain.SourceParametersDomainModel
import dagger.Module
import dagger.Provides

@Module
class MainModule{
    @Provides
    fun provideRepositoryImpl(headlinesNewsRepo: NewsDataBaseRepository<HeadlinesWithNews, HeadlinesParametersDomainModel, NewsResponseDto>,
                              everythingNewsRepo: NewsDataBaseRepository<FilterWithNews, NewsParametersDomainModel, NewsResponseDto>,
                              sourceRepo: NewsDataBaseRepository<SourceWithNews, SourceParametersDomainModel, SourceDomainModel>,
                              saveDataBaseImpl: SaveDataBaseImpl
    ): Repository {
        return RepositoryImpl(
            headlinesNewsRepo,
            everythingNewsRepo,
            sourceRepo,
            saveDataBaseImpl
        )
    }

    @Provides
    fun provideEverythingNewsRepoImpl(context: Context): NewsDataBaseRepository<FilterWithNews, NewsParametersDomainModel, NewsResponseDto> {
        return EverythingNewsRepoImpl(context)
    }

    @Provides
    fun provideHeadlinesNewsRepoImpl(context: Context): NewsDataBaseRepository<HeadlinesWithNews, HeadlinesParametersDomainModel, NewsResponseDto> {
        return HeadlinesNewsRepoImpl(context)
    }

    @Provides
    fun provideSourceRepoImpl(context: Context): NewsDataBaseRepository<SourceWithNews, SourceParametersDomainModel, SourceDomainModel> {
        return SourceRepoImpl(context)
    }

    @Provides
    fun provideSaveDataBaseImpl(context: Context): SaveDataBaseImpl {
        return SaveDataBaseImpl(context)
    }
}