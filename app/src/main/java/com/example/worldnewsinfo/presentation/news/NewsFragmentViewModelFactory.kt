package com.example.worldnewsinfo.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldnewsinfo.domain.Repository
import com.example.worldnewsinfo.presentation.model.NewsModel

class NewsFragmentViewModelFactory(private val newsModel: NewsModel, private val repositoryImpl: Repository, ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsFragmentViewModel(newsModel, repositoryImpl) as T
    }
}