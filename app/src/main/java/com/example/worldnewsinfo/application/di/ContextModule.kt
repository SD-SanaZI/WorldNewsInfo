package com.example.worldnewsinfo.application.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class ContextModule @Inject constructor(context: Context) {

    private val context: Context

    init {
        this.context = context
    }

    @Provides
    fun provideApplicationContext(): Context {
        return context
    }
}