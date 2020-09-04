package com.msproject.myhome.mydays.di

import android.content.Context
import com.msproject.myhome.mydays.di.viewModel.ViewModelFactoryModule
import com.msproject.myhome.mydays.di.viewModel.ViewModelModule
import com.msproject.myhome.mydays.main.MainActivity
import com.msproject.myhome.mydays.main.event.EventActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [EventModule::class, DatabaseModule::class, ViewModelModule::class, ViewModelFactoryModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context:Context):AppComponent
    }
    fun inject(activity: MainActivity)
    fun inject(activity:EventActivity)
}