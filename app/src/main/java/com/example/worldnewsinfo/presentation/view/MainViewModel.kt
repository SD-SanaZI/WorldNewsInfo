package com.example.worldnewsinfo.presentation.view

import androidx.lifecycle.ViewModel
import com.example.worldnewsinfo.presentation.navigationBar.NavBarState
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.subjects.PublishSubject

class MainViewModel: ViewModel() {
    var navBarState: NavBarState = NavBarState.HEADLINES
        set(value) {
            navBarPublish.onNext(value)
            field = value
        }
    private val navBarPublish = PublishSubject.create<NavBarState>()

    fun navBarSubscribe(observer: Observer<NavBarState>){
        navBarPublish.subscribe(observer)
    }
}

