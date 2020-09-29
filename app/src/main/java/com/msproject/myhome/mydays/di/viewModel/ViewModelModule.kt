package com.msproject.myhome.mydays.di.viewModel

import androidx.lifecycle.ViewModel
import com.msproject.myhome.mydays.main.dailygraph.DailyGraphViewModel
import com.msproject.myhome.mydays.main.fragment.PlannerViewModel
import com.msproject.myhome.mydays.main.fragment.DetailViewModel
import com.msproject.myhome.mydays.main.event.CategoryDialogViewModel
import com.msproject.myhome.mydays.main.event.EventViewModel
import com.msproject.myhome.mydays.main.statistic.StatisticActivity
import com.msproject.myhome.mydays.main.statistic.StatisticViewModel
import com.msproject.myhome.mydays.main.toolbar.ToolbarViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlannerViewModel::class)
    abstract fun mainViewModel(viewModel: PlannerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel::class)
    abstract fun eventViewModel(viewModel: EventViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(CategoryDialogViewModel::class)
    abstract fun categoryViewModel(viewModel: CategoryDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun detailViewModel(viewModel: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ToolbarViewModel::class)
    abstract fun toolbarViewModel(viewModel:ToolbarViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DailyGraphViewModel::class)
    abstract fun dailyGraphVieModel(viewModel:DailyGraphViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StatisticViewModel::class)
    abstract fun statisticViewModel(viewModel:StatisticViewModel):ViewModel

}