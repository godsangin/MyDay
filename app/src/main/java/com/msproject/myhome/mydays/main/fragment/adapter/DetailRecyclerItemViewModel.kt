package com.msproject.myhome.mydays.main.fragment.adapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.msproject.myhome.mydays.model.Category
import com.msproject.myhome.mydays.model.ChartCategory
import com.msproject.myhome.mydays.model.EventItem
import com.msproject.myhome.mydays.utils.SingleLiveEvent

class DetailRecyclerItemViewModel() {
    val _dailyTime = MutableLiveData<List<Int>>()
    val _chartCategory = MutableLiveData<List<Pair<Category, Int>>>()
    val startActivityEvent = SingleLiveEvent<Unit>()
    val day = MutableLiveData<Int>()
    val dailyTime:LiveData<List<Int>> get() = _dailyTime
    val chartCategory:LiveData<List<Pair<Category, Int>>> get() = _chartCategory

    fun startDailyGraphActivity(){
        startActivityEvent.call()
    }
}