package com.msproject.myhome.mydays.main.dailygraph

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.msproject.myhome.mydays.R
import com.msproject.myhome.mydays.application.MyApplication
import com.msproject.myhome.mydays.databinding.ActivityDailyGraphBinding
import com.msproject.myhome.mydays.main.toolbar.ToolbarViewModel
import kotlinx.android.synthetic.main.activity_daily_graph.view.*
import javax.inject.Inject

class DailyGraphActivity : AppCompatActivity(), OnChartValueSelectedListener{
    lateinit var dailyGraphViewModel: DailyGraphViewModel
    lateinit var toolbarViewModel: ToolbarViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.inject(this)
        val binding = DataBindingUtil.setContentView<ActivityDailyGraphBinding>(this, R.layout.activity_daily_graph)
        dailyGraphViewModel = ViewModelProviders.of(this, viewModelFactory)[DailyGraphViewModel::class.java]
        toolbarViewModel = ViewModelProviders.of(this, viewModelFactory)[ToolbarViewModel::class.java]
        binding.lifecycleOwner = this
        val owner = this
        toolbarViewModel.apply {
            titleText.postValue(getString(R.string.text_title_daily_graph))
            backButtonEvent.observe(owner, Observer {
                onBackPressed()
            })
        }
        dailyGraphViewModel.initData(this)
        binding.model = dailyGraphViewModel
        binding.toolbarModel = toolbarViewModel
        setChartClickListener(binding.root)
    }
    
    fun setChartClickListener(view:View){
        view.chart.setOnChartValueSelectedListener(this)
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        dailyGraphViewModel.setDate(e?.x?.toInt() ?: 6)
    }
}