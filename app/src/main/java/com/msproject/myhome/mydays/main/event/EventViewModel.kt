package com.msproject.myhome.mydays.main.event

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

class EventViewModel @Inject constructor(private val eventRepository: EventRepository, private val categoryRepository: CategoryRepository):ViewModel() {
    val _eventList = MutableLiveData<List<ChartData>>()
    val _date = MutableLiveData<Date>()

    val eventList: LiveData<List<ChartData>> get() = _eventList
    val date: LiveData<Date> get() = _date

    fun initBaseData(type:Int, dateString:String, owner: LifecycleOwner){
        //date + time 조합해서 repository에서 불러오기
        val format = SimpleDateFormat("yyyy-MM-dd")
        _date.postValue(format.parse(dateString))
        val calendar = Calendar.getInstance()
        calendar.time = date.value
        calendar.add(Calendar.HOUR, 6*type)
        var startDate = format.parse(format.format(calendar.time)).time
        calendar.add(Calendar.HOUR, 6)
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
                        for(i in 0..((item.startDate - startDate) / 3600000).toInt()){
                            val emptyData = ChartData("", "", "#ffffff")
                            chartDataList.add(emptyData)
                        }
                    }
                    val category = categoryRepository.getCategoryById(item.cid)
                    for(i in 0..((item.endDate - item.startDate) / 3600000).toInt()){
                        chartDataList.add(ChartData(category.name, item.content, category.color))
                    }
                    startDate = item.endDate
                }
                _eventList.postValue(chartDataList)
            }
        })
    }
}