package com.example.worldnewsinfo.presentation.news

import androidx.lifecycle.ViewModel
import com.example.worldnewsinfo.domain.Repository
import com.example.worldnewsinfo.presentation.mapper.NewsDomainModelMapper
import com.example.worldnewsinfo.presentation.model.NewsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsFragmentViewModel(private val newsModel: NewsModel, val repositoryImpl: Repository): ViewModel() {

    private val mutableStateFlow = MutableStateFlow<NewsState>(NewsState())
    val stateFlow: StateFlow<NewsState>
        get() = mutableStateFlow


    init {
        val newsDomainModel = NewsDomainModelMapper().mapNewsModel(newsModel)
        CoroutineScope(Dispatchers.IO).launch {
            setSavedState(repositoryImpl.areNewsSaved(newsDomainModel))
        }
    }

    private fun setSavedState(state: Boolean){
        mutableStateFlow.value = mutableStateFlow.value.copy(isSaved = state)
    }

    fun changeSavedState(){
        val newsDomainModel = NewsDomainModelMapper().mapNewsModel(newsModel)
        CoroutineScope(Dispatchers.IO).launch {
            if (mutableStateFlow.value.isSaved){
                repositoryImpl.deleteNews(newsDomainModel)
                setSavedState(false)
            }else{
                repositoryImpl.saveNews(newsDomainModel)
                setSavedState(true)
            }
        }
    }
}