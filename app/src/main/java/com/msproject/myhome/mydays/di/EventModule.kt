package com.msproject.myhome.mydays.di

import com.msproject.myhome.mydays.repository.CategoryRepository
import com.msproject.myhome.mydays.repository.EventRepository
import com.msproject.myhome.mydays.repository.impl.CategoryRepositoryImpl
import com.msproject.myhome.mydays.repository.impl.EventRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class EventModule{
    @Binds
    abstract fun provideCategoryRepository(categoryRepository: CategoryRepositoryImpl):CategoryRepository
    @Binds
    abstract fun provideEventRepository(eventRepository: EventRepositoryImpl):EventRepository
}