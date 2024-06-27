package com.example.worldnewsinfo.application

import android.app.Application
import androidx.room.Room
import com.example.worldnewsinfo.application.di.ApplicationComponent
import com.example.worldnewsinfo.application.di.ContextModule
import com.example.worldnewsinfo.application.di.DaggerApplicationComponent
import com.example.worldnewsinfo.data.room.save.SAVE_DATABASE_NAME
import com.example.worldnewsinfo.data.room.save.SaveDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TWO_WEEKS_IN_MILLIS = 1000L * 60 * 60 * 24 * 7 * 2

class MainApplication: Application() {
    @Inject lateinit var contextModule: ContextModule

    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .androidModule(ContextModule(this))
            .build()
        cleanOldSavedNews()
    }

    private fun cleanOldSavedNews(){
        val db = Room
            .databaseBuilder(this, SaveDatabase::class.java, SAVE_DATABASE_NAME)
            .build()
        val dao = db.SaveNewsDao()
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteLater(System.currentTimeMillis() - TWO_WEEKS_IN_MILLIS)
        }
    }
}


