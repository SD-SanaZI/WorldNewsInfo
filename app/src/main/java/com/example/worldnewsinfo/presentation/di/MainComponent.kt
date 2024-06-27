package com.example.worldnewsinfo.presentation.di

import android.content.Context
import com.example.worldnewsinfo.presentation.find.FindFragment
import com.example.worldnewsinfo.presentation.headlines.HeadlinesPresenter
import com.example.worldnewsinfo.presentation.news.NewsFragment
import com.example.worldnewsinfo.presentation.saved.SavedFragment
import com.example.worldnewsinfo.presentation.view.MainActivity
import com.example.worldnewsinfo.presentation.source.SourceFragment
import com.example.worldnewsinfo.presentation.sourseNews.SourceNewsFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [MainModule::class]
)
interface MainComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(sourceFragment: SourceFragment)
    fun inject(findFragment: FindFragment)
    fun inject(sourceNewsFragment: SourceNewsFragment)
    fun inject(newsFragment: NewsFragment)
    fun inject(savedFragment: SavedFragment)
    fun inject(headlinesPresenter: HeadlinesPresenter)

    @Component.Builder
    interface Builder{
        fun mainModule(@BindsInstance context: Context):Builder
        fun build():MainComponent
    }

    companion object{
        fun create(context: Context):MainComponent{
            return DaggerMainComponent.builder().mainModule(context).build()
        }
    }
}