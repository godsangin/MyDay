package com.msproject.myhome.mydays.main.challenge

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.msproject.myhome.mydays.model.ProgressItem
import com.msproject.myhome.mydays.repository.EventRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ChallengeViewModel @Inject constructor(private val eventRepository: EventRepository):ViewModel(){
    val _dailyProgressItem = MutableLiveData<ProgressItem>()
    val _weeklyProgressItem = MutableLiveData<ProgressItem>()
    val _monthlyProgressItem = MutableLiveData<ProgressItem>()
    val _totalProgressItem = MutableLiveData<ProgressItem>()

    val dailyProgressItem:LiveData<ProgressItem> get() = _dailyProgressItem
    val weeklyProgressItem:LiveData<ProgressItem> get() = _weeklyProgressItem
    val monthlyProgressItem:LiveData<ProgressItem> get() = _monthlyProgressItem
    val totalProgressItem:LiveData<ProgressItem> get() = _totalProgressItem

    fun initData(owner:LifecycleOwner){
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateString = format.format(Date())
        val dateLong = format.parse(dateString).time
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        val dayLong = (24 * 60 * 60 * 1000).toLong()
        calendar.timeInMillis = dateLong
        val weekStartLong = dateLong - (dayLong * (calendar.get(Calendar.DAY_OF_WEEK) - 1))
        val weekEndLong = weekStartLong + (dayLong * 6)
        val monthStartLong = dateLong - (dayLong * calendar.get(Calendar.DAY_OF_MONTH))
        val monthEndLong = monthStartLong + (dayLong * calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        eventRepository.getEventListCount(dateLong, dateLong).observe(owner, Observer {
            _dailyProgressItem.postValue(ProgressItem("일일 달성률", 24, it))
        })
        eventRepository.getEventListCount(weekStartLong, weekEndLong).observe(owner, Observer {
            _weeklyProgressItem.postValue(ProgressItem("주간 달성률", 24 * 7, it))
        })
        eventRepository.getEventListCount(monthStartLong, monthEndLong).observe(owner, Observer {
            _monthlyProgressItem.postValue(ProgressItem("월간 달성률", 24 * calendar.getActualMaximum(Calendar.DAY_OF_MONTH), it))
        })
        eventRepository.getEventListCount().observe(owner, Observer {
            _totalProgressItem.postValue(ProgressItem("누적 계획 시간", it, it))
        })
    }
}