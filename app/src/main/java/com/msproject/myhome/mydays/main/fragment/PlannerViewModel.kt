package com.msproject.myhome.mydays.main.fragment

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.msproject.myhome.mydays.model.CalendarItem
import com.msproject.myhome.mydays.model.Category
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
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject


class PlannerViewModel @Inject constructor(private val eventRepository: EventRepository, private val categoryRepository: CategoryRepository):ViewModel(){
    //usecase(getDailyEvents -> chart insert, getDailyMemo, insertMemo)
    //field(eventPartitionView, events, eventStatisticView, eventCalendarDialog, eventSettingView)
    val _eventList = MutableLiveData<List<ChartData>>()
    val _weeklyCategoryList = MutableLiveData<List<CalendarItem>>()
    val _date = MutableLiveData<Date>()
    val _isPlanner = MutableLiveData<Boolean>(true)
    val showCalendarEvent = SingleLiveEvent<Unit>()

    val eventList:LiveData<List<ChartData>> get() = _eventList
    val weeklyCategoryList:LiveData<List<CalendarItem>> get() = _weeklyCategoryList
    val date:LiveData<Date> get() = _date
    val isPlanner:LiveData<Boolean> get() = _isPlanner
    val day = MutableLiveData<Int>(0)
    var eventObserver:LiveData<List<Event>>? = null
    var weeklyEventObserver:ArrayList<LiveData<List<Event>>>? = null
    var firstLoad = AtomicBoolean(true)

    fun initData(owner:LifecycleOwner){
        date.observe(owner, Observer {
            getDailyData(it, owner)
            getWeeklyData(it, owner)
        })
        _date.postValue(Date())
    }

    fun getDailyData(date:Date, owner:LifecycleOwner) {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateString = format.format(date)
        val dateLong = format.parse(dateString).time
        eventObserver?.removeObservers(owner)
        eventObserver = eventRepository.getEventList(dateLong)
        eventObserver?.observe(owner, Observer {
            CoroutineScope(Dispatchers.IO).launch {
                val chartDataList = ArrayList<ChartData>()
                if (it.isEmpty()) {
                    chartDataList.add(ChartData("", "#ffffff", 24))
                } else {
                    var time = 0
                    var lastItemIndex = 0
                    var lastItem: Event? = null
                    for (item in it) {
                        val category = categoryRepository.getCategoryById(item.cid)
                        if (time != item.time) {
                            chartDataList.add(ChartData("", "#ffffff", item.time - time))
                            lastItem = null
                        }
                        if (item.cid == lastItem?.cid) {
                            chartDataList.get(lastItemIndex).size++
                        } else {
                            chartDataList.add(ChartData(category?.name ?: "", category?.color
                                    ?: "", 1))
                            lastItemIndex = chartDataList.size - 1
                        }
                        lastItem = item
                        time = item.time + 1
                    }
                    if (time != 23) {
                        chartDataList.add(ChartData("", "#ffffff", 24 - time))
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

    fun getWeeklyData(date:Date, owner:LifecycleOwner){
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateString = format.format(date)
        val dateLong = format.parse(dateString).time
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        calendar.timeInMillis = dateLong
        val dayLong = (24 * 60 * 60 * 1000).toLong()
        val weekStartLong = dateLong - (dayLong * (calendar.get(Calendar.DAY_OF_WEEK) - 1))
        day.postValue(calendar.get(Calendar.DAY_OF_MONTH))
        if(!firstLoad.get() && (calendar.get(Calendar.DAY_OF_WEEK) != 1 && calendar.get(Calendar.DAY_OF_WEEK) != 7)) return
        if(firstLoad.get()){
            firstLoad.set(false)
        }
        val weekItemList = ArrayList<CalendarItem>()
        calendar.timeInMillis = weekStartLong
        for(i in 0..6){
            weeklyEventObserver?.get(i)?.removeObservers(owner)
            weekItemList.add(CalendarItem("", ArrayList()))
        }
        weeklyEventObserver = ArrayList()
        for(i in 0..6){
            val categoryList = ArrayList<Category>()
            val eventObserver = eventRepository.getEventList(weekStartLong + (i * dayLong))
            eventObserver.observe(owner, Observer {
                CoroutineScope(Dispatchers.IO).launch {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = weekStartLong + (i * dayLong)
                    for(item in it){
                        val category = categoryRepository.getCategoryById(item.cid)
                        if(!categoryList.contains(category)){
                            categoryList.add(category ?: Category())
                        }
                    }
                    weekItemList.set(i, CalendarItem(calendar.get(Calendar.DAY_OF_MONTH).toString(), categoryList))
                    _weeklyCategoryList.postValue(weekItemList)
                }
            })
            weeklyEventObserver?.add(eventObserver)
        }
    }

    fun setDay(day:Int){
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateString = format.format(date.value)
        val dateLong = format.parse(dateString).time
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        calendar.timeInMillis = dateLong
        val dayLong = (24 * 60 * 60 * 1000).toLong()
        val weekStartLong = dateLong - (dayLong * (calendar.get(Calendar.DAY_OF_WEEK) - 1))
        val newDate = Date()
        newDate.time = weekStartLong + (day * dayLong)
        _date.postValue(newDate)
    }
}