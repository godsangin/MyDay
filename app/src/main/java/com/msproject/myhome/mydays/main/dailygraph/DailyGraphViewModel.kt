package com.msproject.myhome.mydays.main.dailygraph

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.msproject.myhome.mydays.model.Category
import com.msproject.myhome.mydays.model.ChartCategory
import com.msproject.myhome.mydays.model.Event
import com.msproject.myhome.mydays.repository.CategoryRepository
import com.msproject.myhome.mydays.repository.EventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class DailyGraphViewModel @Inject constructor(private val eventRepository: EventRepository, private val categoryRepository: CategoryRepository) :ViewModel(){
    val _dailyTime = MutableLiveData<List<Int>>()
    val _chartCategoryList = MutableLiveData<List<ChartCategory>>()

    val dateLiveData = MutableLiveData<Date>()
    val toDay = MutableLiveData<Int>()
    val dailyTime:LiveData<List<Int>> get() = _dailyTime
    val chartCategoryList:LiveData<List<ChartCategory>> get() = _chartCategoryList

    fun initData(owner:LifecycleOwner){
        //최근 7일 데이터로 dailyTime구하기
        //date세팅
        //date에 따라 _eventList 바뀜
        //eventList에 따라 chartCategoryList바낌

        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateString = format.format(Date())
        val dateLong = format.parse(dateString).time
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        calendar.timeInMillis = dateLong
        toDay.postValue(calendar.get(Calendar.DAY_OF_WEEK) - 1)

        val startDateLong = dateLong - (24 * 60 * 60 * 1000 * 6)
        dateLiveData.postValue(format.parse(dateString))
        eventRepository.getEventList(startDateLong, dateLong).observe(owner, Observer {
            val timeArray = ArrayList<Int>()
            for(i in 0..6){
                timeArray.add(0)
            }
            for(item in it){
                timeArray[((item.date - startDateLong) / (24 * 60 * 60 * 1000)).toInt()]++
            }
            _dailyTime.postValue(timeArray)
        })
        dateLiveData.observe(owner, Observer {
            eventRepository.getEventList(it.time).observe(owner, Observer {
                val cidMap = HashMap<Long, Int>()
                for(item in it){
                    if(!cidMap.contains(item.cid)){
                        cidMap.put(item.cid, 0)
                    }
                    else{
                        cidMap.set(item.cid, ((cidMap.get(item.cid) ?: 0) + 1))
                    }
                }
                val cidList = cidMap.toList()
                cidList.sortedBy {
                    it.second
                }
                val chartList = ArrayList<ChartCategory>()
                CoroutineScope(Dispatchers.IO).launch {
                    for(pair in cidList){
                        val category = categoryRepository.getCategoryById(pair.first)
                        chartList.add(ChartCategory(category ?: Category(), (pair.second.toDouble() * 100 / it.size).toInt().toString() + "%"))
                    }
                    _chartCategoryList.postValue(chartList)
                }
            })
        })
    }

    fun setDate(pos:Int){
        dateLiveData.value.run {
            if(this == null) return
            var time = time
            time = time?.minus((24 * 60 * 60 * 1000 * (6 - pos)).toLong())
            dateLiveData.postValue(Date(time))
        }
    }
}