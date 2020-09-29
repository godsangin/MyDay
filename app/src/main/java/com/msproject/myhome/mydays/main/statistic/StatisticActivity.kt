package com.msproject.myhome.mydays.main.statistic

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.msproject.myhome.mydays.R
import com.msproject.myhome.mydays.application.MyApplication
import com.msproject.myhome.mydays.base.BaseActivity
import com.msproject.myhome.mydays.databinding.ActivityStatisticBinding
import com.msproject.myhome.mydays.main.toolbar.ToolbarViewModel
import java.text.SimpleDateFormat
import javax.inject.Inject

class StatisticActivity : BaseActivity() {
    lateinit var statisticViewModel:StatisticViewModel
    lateinit var toolbarViewModel: ToolbarViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.inject(this)
        val binding = DataBindingUtil.setContentView<ActivityStatisticBinding>(this, R.layout.activity_statistic)
        statisticViewModel = ViewModelProviders.of(this, viewModelFactory)[StatisticViewModel::class.java]
        toolbarViewModel = ViewModelProviders.of(this, viewModelFactory)[ToolbarViewModel::class.java]
        binding.lifecycleOwner = this
        binding.model = statisticViewModel
        binding.toolbarModel = toolbarViewModel
        val owner = this
        toolbarViewModel.apply {
            titleText.postValue(getString(R.string.text_title_classification_graph))
            backButtonEvent.observe(owner, Observer {
                onBackPressed()
            })
        }
        statisticViewModel.apply {
            initDate(owner)
            setStartDateEvent.observe(owner, Observer {
                showDatePickerDialog(getStartDateCallback())
            })
            setEndDateEvent.observe(owner, Observer {
                showDatePickerDialog(getEndDateCallback())
            })
        }
    }

    private fun showDatePickerDialog(datePickerCallback:DatePickerDialog.OnDateSetListener){
        val datePickerDialog = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DatePickerDialog(MainActivity@this)
        } else {
            null
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            datePickerDialog?.setOnDateSetListener(datePickerCallback)
            datePickerDialog?.show()
        }
    }

    fun getStartDateCallback():DatePickerDialog.OnDateSetListener{
        return object:DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                val monthText = if(month + 1 < 10) "0" + (month+1).toString() else (month+1).toString()
                val dayText = if(dayOfMonth < 10) "0" + (dayOfMonth).toString() else (dayOfMonth).toString()
                val text = year.toString() + "-" + monthText + "-" + dayText
                val format = SimpleDateFormat("yyyy-MM-dd")
                statisticViewModel._startDate.postValue(format.parse(text).time)
            }
        }
    }

    fun getEndDateCallback():DatePickerDialog.OnDateSetListener{
        return object:DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                val monthText = if(month + 1 < 10) "0" + (month+1).toString() else (month+1).toString()
                val dayText = if(dayOfMonth < 10) "0" + (dayOfMonth).toString() else (dayOfMonth).toString()
                val text = year.toString() + "-" + monthText + "-" + dayText
                val format = SimpleDateFormat("yyyy-MM-dd")
                statisticViewModel._endDate.postValue(format.parse(text).time)
            }
        }
    }
}