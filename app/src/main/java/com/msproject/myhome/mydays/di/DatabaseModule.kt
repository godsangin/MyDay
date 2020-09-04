package com.msproject.myhome.mydays.di

import android.content.Context
import androidx.room.Room
import com.msproject.myhome.mydays.repository.room.AppDatabase
import dagger.Module
import dagger.Provides
@Module
class DatabaseModule {
    @Provides
    fun provideDatabase(context: Context):AppDatabase{
        return Room.databaseBuilder(context, AppDatabase::class.java, "event.db").build()
    }
}