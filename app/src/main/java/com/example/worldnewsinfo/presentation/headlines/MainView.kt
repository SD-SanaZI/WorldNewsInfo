package com.example.worldnewsinfo.presentation.headlines

import com.example.worldnewsinfo.presentation.model.NewsModel
import io.reactivex.rxjava3.core.Observable
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MainView : MvpView {
    @AddToEndSingle
    fun setRecyclerAdapter(list: Observable<List<NewsModel>>)
}