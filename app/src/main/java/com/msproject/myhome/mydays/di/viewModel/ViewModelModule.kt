package com.msproject.myhome.mydays.di.viewModel

import androidx.lifecycle.ViewModel
import com.msproject.myhome.mydays.main.MainViewModel
import com.msproject.myhome.mydays.main.event.CategoryDialogViewModel
import com.msproject.myhome.mydays.main.event.EventViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel::class)
    abstract fun eventViewModel(viewModel: EventViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(CategoryDialogViewModel::class)
    abstract fun categoryViewModel(viewModel: CategoryDialogViewModel): ViewModel

}