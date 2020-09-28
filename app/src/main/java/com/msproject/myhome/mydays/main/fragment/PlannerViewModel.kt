package com.msproject.myhome.mydays.main.fragment

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.msproject.myhome.mydays.model.ChartData
import com.msproject.myhome.mydays.model.Event
import com.msproject.myhome.mydays.repository.CategoryRepository
import com.msproject.myhome.mydays.repository.EventRepository
import com.msproject.myhome.mydays.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class PlannerViewModel @Inject constructor(private val eventRepository: EventRepository, private val categoryRepository: CategoryRepository):ViewModel(){
    //usecase(getDailyEvents -> chart insert, getDailyMemo, insertMemo)
    //field(eventPartitionView, events, eventStatisticView, eventCalendarDialog, eventSettingView)
    val _eventList = MutableLiveData<List<ChartData>>()
    val _date = MutableLiveData<Date>()
    val _isPlanner = MutableLiveData<Boolean>(true)
    val showCalendarEvent = SingleLiveEvent<Unit>()

    val eventList:LiveData<List<ChartData>> get() = _eventList
    val date:LiveData<Date> get() = _date
    val isPlanner:LiveData<Boolean> get() = _isPlanner

    fun initData(owner:LifecycleOwner){
        date.observe(owner, Observer {
            getDailyData(it, owner)
        })
        _date.postValue(Date())
    }

    fun getDailyData(date:Date, owner:LifecycleOwner){
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateString = format.format(date)
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
                            chartDataList.add(ChartData(category?.name ?:"", item.content, category?.color ?:"", 1))
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
    fun prevDate(){
        var time = date.value?.time ?: 0
        time -= (24 * 60 * 60 * 1000)
        _date.postValue(Date(time))
    }

    fun nextDate(){
        var time = date.value?.time ?: 0
        time += (24 * 60 * 60 * 1000)
        _date.postValue(Date(time))
    }

    fun getToday(){
        _date.postValue(Date())
    }

    fun showCalendarDialog(){
        showCalendarEvent.call()
    }

    fun setMode(planner:Boolean){
        _isPlanner.postValue(planner)
    }
}