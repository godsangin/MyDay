package com.msproject.myhome.mydays.main

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.msproject.myhome.mydays.model.ChartData
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
    val _date = MutableLiveData<Date>()

    val eventList:LiveData<List<ChartData>> get() = _eventList
    val date:LiveData<Date> get() = _date

    init {
        _date.postValue(Date())
    }

    fun initData(owner:LifecycleOwner){
        //date에 따라 usecase로 나누기
        val calendar = Calendar.getInstance()
        calendar.time = date.value
        calendar.add(Calendar.DATE, 1)
        val format = SimpleDateFormat("yyyy-MM-dd")
        var startDate = format.parse(format.format(date.value)).time
        val endDate = format.parse(format.format(calendar.time)).time
        eventRepository.getEventList(startDate, endDate).observe(owner, Observer {
            CoroutineScope(Dispatchers.IO).launch {
                val chartDataList = ArrayList<ChartData>()
                if(it.isEmpty()){
                    chartDataList.add(ChartData("","","#ffffff", 24))
                }
                for(item in it){
                    if(item.startDate < startDate){
                        item.startDate = startDate
                    }
                    if(item.endDate > endDate){
                        item.endDate = endDate
                    }
                    if(item.startDate > startDate){
                        val emptyData = ChartData("", "", "#ffffff", ((item.startDate - startDate) / 3600000).toInt())
                        chartDataList.add(emptyData)
                    }
                    val category = categoryRepository.getCategoryById(item.cid)
                    chartDataList.add(ChartData(category.name, item.content, category.color, ((item.endDate - item.startDate) / 3600000).toInt()))
                    startDate = item.endDate
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