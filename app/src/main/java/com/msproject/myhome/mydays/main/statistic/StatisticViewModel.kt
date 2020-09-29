package com.msproject.myhome.mydays.main.statistic

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.msproject.myhome.mydays.model.Category
import com.msproject.myhome.mydays.repository.CategoryRepository
import com.msproject.myhome.mydays.repository.EventRepository
import com.msproject.myhome.mydays.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class StatisticViewModel @Inject constructor(private val eventRepository: EventRepository, private val categoryRepository: CategoryRepository) :ViewModel(){
    val _chartCategory = MutableLiveData<List<Pair<Category, Int>>>()
    val _startDate = MutableLiveData<Long>()
    val _endDate = MutableLiveData<Long>()

    val setStartDateEvent = SingleLiveEvent<Unit>()
    val setEndDateEvent = SingleLiveEvent<Unit>()
    val chartCategory: LiveData<List<Pair<Category, Int>>> get() = _chartCategory
    val startDate:LiveData<Long> get() = _startDate
    val endDate:LiveData<Long> get() = _endDate

    fun initDate(owner:LifecycleOwner){
        startDate.observe(owner, Observer {
            if(endDate.value == null){
                return@Observer
            }
            findCategoryByEvent(it, endDate.value ?: 0.toLong())
        })
        endDate.observe(owner, Observer {
            if(startDate.value == null){
                return@Observer
            }
            findCategoryByEvent(startDate.value ?: 0.toLong(), it)
        })
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateString = format.format(Date())
        val dateLong = format.parse(dateString).time
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        calendar.timeInMillis = dateLong
        val startDateLong = dateLong - (24 * 60 * 60 * 1000 * 6)
        _startDate.postValue(startDateLong)
        _endDate.postValue(dateLong)
    }

    fun findCategoryByEvent(startDate:Long, endDate:Long){
        CoroutineScope(Dispatchers.IO).launch {
            val eventList = eventRepository.getEventListSync(startDate, endDate)
            val cidMap = HashMap<Long, Int>()
            for(event in eventList ?: ArrayList()){
                if(!cidMap.contains(event.cid)){
                    cidMap.put(event.cid, 1)
                }
                else{
                    cidMap.set(event.cid, ((cidMap.get(event.cid) ?: 1) + 1))
                }
            }
            val categoryList = ArrayList<Pair<Category, Int>>()
            for(cid in cidMap.keys){
                val category = categoryRepository.getCategoryById(cid)
                categoryList.add(Pair(category ?: Category(), cidMap[cid] ?: -1))
            }
            _chartCategory.postValue(categoryList)
        }
    }

    fun setStartDate(){
        setStartDateEvent.call()
    }
    fun setEndDate(){
        setEndDateEvent.call()
    }
}