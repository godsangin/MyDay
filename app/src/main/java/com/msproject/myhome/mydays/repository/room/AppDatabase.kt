package com.msproject.myhome.mydays.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.msproject.myhome.mydays.model.Category
import com.msproject.myhome.mydays.model.Event
import javax.inject.Singleton

@Singleton
@Database(entities = [Event::class, Category::class], version = 1, exportSchema = false)
abstract class AppDatabase :RoomDatabase(){
    abstract fun getEventDao():EventDao
    abstract fun getCategoryDao():CategoryDao
}