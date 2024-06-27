package com.example.worldnewsinfo.application.di

import com.example.worldnewsinfo.application.MainApplication
import com.example.worldnewsinfo.data.RepositoryImpl
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class])
interface ApplicationComponent {
    fun inject(application: MainApplication)
    fun inject(repositoryImpl: RepositoryImpl)

    @Component.Builder
    interface Builder{
        fun androidModule(contextModule: ContextModule): Builder

        fun build(): ApplicationComponent
    }
}