package com.msproject.myhome.mydays.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.msproject.myhome.mydays.repository.room.AppDatabase
import dagger.Module
import dagger.Provides
@Module
class DatabaseModule {
    @Provides
    fun provideDatabase(context: Context):AppDatabase{
        return Room.databaseBuilder(context, AppDatabase::class.java, "event.db")
                .addCallback(object: RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        db.execSQL("insert into category (name, color) values ('수면', '#123456');")
                        db.execSQL("insert into category (name, color) values ('공부', '#ff0000');")
                        db.execSQL("insert into category (name, color) values ('운동', '#4169e1');")
                    }
                })
                .build()
    }
}