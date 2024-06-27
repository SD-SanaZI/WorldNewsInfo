package com.example.worldnewsinfo.data.room.save

import androidx.room.Database
import androidx.room.RoomDatabase

const val SAVE_DATABASE_NAME = "SAVE_DATABASE_NAME"

@Database(entities = [SavedNews::class], version = 1)
abstract class SaveDatabase : RoomDatabase() {
    abstract fun SaveNewsDao(): SaveNewsDao
}