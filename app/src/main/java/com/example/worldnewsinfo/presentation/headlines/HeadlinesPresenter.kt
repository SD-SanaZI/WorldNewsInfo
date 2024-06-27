package com.example.worldnewsinfo.presentation.headlines

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.worldnewsinfo.domain.HeadlinesParametersDomainModel
import com.example.worldnewsinfo.domain.LoadNewsFromEverythingUseCase
import com.example.worldnewsinfo.domain.LoadingNewsUseCase
import com.example.worldnewsinfo.domain.NewsParametersDomainModel
import com.example.worldnewsinfo.domain.Repository
import com.example.worldnewsinfo.presentation.di.MainComponent
import com.example.worldnewsinfo.presentation.filter.FilterState
import com.example.worldnewsinfo.presentation.mapper.NewsModelMapper
import com.example.worldnewsinfo.presentation.mapper.NewsParametersDomainModelMapper
import com.example.worldnewsinfo.presentation.model.NewsModel
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

class HeadlinesPresenter : MvpPresenter<MainView>() {
    @Inject
    lateinit var repositoryImpl: Repository
    private var newsList = listOf<NewsModel>()
    private var pages = 0
    private val _activeHeadlinesTab: MutableLiveData<HeadlinesTab> by lazy {
        MutableLiveData<HeadlinesTab>(HeadlinesTab.GENERAL)
    }
    val activeHeadlinesTab: LiveData<HeadlinesTab>
        get() = _activeHeadlinesTab

    fun setActiveHeadlinesTab(headlinesTab: HeadlinesTab) {
        _activeHeadlinesTab.value = headlinesTab
    }

    override fun onFirstViewAttach() {
        val observable = Observable.just(newsList)
        viewState.setRecyclerAdapter(observable)
    }

    fun setNewsList(filter: FilterState, context: Context) {
        MainComponent.create(context).inject(this)
        pages = 0
        presenterScope.launch {
            newsList = updateNewsList(filter)
            withContext(Dispatchers.Main) {
                val observable = Observable.just(newsList)
                viewState.setRecyclerAdapter(observable)
            }
        }
    }

    private suspend fun updateNewsList(filter: FilterState): List<NewsModel> {
        return if (getFilterCount(filter) != 0)
            getEverythingNewsList(
                NewsParametersDomainModelMapper()
                    .mapNewsParameters(filter, page = pages)
            )
        else
            getHeadlinesNewsList()
    }

    fun expandList(filter: FilterState, context: Context) {
        MainComponent.create(context).inject(this)
        pages++
        presenterScope.launch {
            newsList += updateNewsList(filter)
            withContext(Dispatchers.Main) {
                val observable = Observable.just(newsList)
                viewState.setRecyclerAdapter(observable)
            }
        }
    }

    private fun getFilterCount(filter: FilterState): Int {
        var count = 0
        if (filter.filterSortList.find { it.state } != null) count++
        if (filter.date != null) count++
        if (filter.filterLanguage != null) count++
        return count
    }

    private suspend fun getHeadlinesNewsList(): List<NewsModel> {
        val repository: Repository = repositoryImpl
        return LoadingNewsUseCase(
            repository,
            HeadlinesParametersDomainModel(
                category = (activeHeadlinesTab.value ?: HeadlinesTab.GENERAL).name, page = pages
            )
        ).invoke()
            .map { NewsModelMapper().mapNewsDomainModel(it) }
    }

    private suspend fun getEverythingNewsList(newsParametersDomainModel: NewsParametersDomainModel): List<NewsModel> {
        val repository: Repository = repositoryImpl
        return LoadNewsFromEverythingUseCase(
            repository,
            newsParametersDomainModel
        ).invoke()
            .map { NewsModelMapper().mapNewsDomainModel(it) }
    }
}