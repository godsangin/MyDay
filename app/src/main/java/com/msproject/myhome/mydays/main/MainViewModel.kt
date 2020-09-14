package com.msproject.myhome.mydays.main

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.msproject.myhome.mydays.model.ChartData
import com.msproject.myhome.mydays.model.Event
import com.msproject.myhome.mydays.repository.CategoryRepository
import com.msproject.myhome.mydays.repository.EventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class MainViewModel @Inject constructor(private val eventRepository: EventRepository, private val categoryRepository: CategoryRepository):ViewModel(){
    //usecase(getDailyEvents -> chart insert, getDailyMemo, insertMemo)
    //field(eventPartitionView, events, eventStatisticView, eventCalendarDialog, eventSettingView)
    val _eventList = MutableLiveData<List<ChartData>>()
    val _date = MutableLiveData<Date>(Date())

    val eventList:LiveData<List<ChartData>> get() = _eventList
    val date:LiveData<Date> get() = _date

    fun initData(owner:LifecycleOwner){
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateString = format.format(date.value)
        val dateLong = format.parse(dateString).time
        eventRepository.getEventList(dateLong).observe(owner, Observer {
            CoroutineScope(Dispatchers.IO).launch {
                val chartDataList = ArrayList<ChartData>()
                if(it.isEmpty()){
                    chartDataList.add(ChartData("","","#ffffff", 24))
                }
                else {
                    var time = 0
                    var lastItemIndex = 0
                    var lastItem: Event? = null
                    for (item in it) {
                        val category = categoryRepository.getCategoryById(item.cid)
                        if (time != item.time) {
                            chartDataList.add(ChartData("", "", "#ffffff", item.time - time))
                            lastItem = null
                        }
                        if (item.cid == lastItem?.cid) {
                            chartDataList.get(lastItemIndex).size++
                        } else {
                            chartDataList.add(ChartData(category.name, item.content, category.color, 1))
                            lastItemIndex = chartDataList.size - 1
                        }
                        lastItem = item
                        time = item.time + 1
                    }
                    if (time != 23) {
                        chartDataList.add(ChartData("", "", "#ffffff", 24 - time))
                    }
                }
                _eventList.postValue(chartDataList)
            }
        })
    }
    fun initDataTest(){
        val chartDataList = ArrayList<ChartData>()
        chartDataList.add(ChartData("", "", "#eeeeee", 4))
        chartDataList.add(ChartData("수면", "숙면", "#123456", 4))
        chartDataList.add(ChartData("운동", "운동", "#345678", 1))
        chartDataList.add(ChartData("", "", "#eeeeee", 4))
        chartDataList.add(ChartData("공부", "수학공부", "#987654", 4))
        chartDataList.add(ChartData("", "", "#eeeeee", 7))
        _eventList.value = chartDataList
    }
}