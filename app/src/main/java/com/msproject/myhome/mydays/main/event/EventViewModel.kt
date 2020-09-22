package com.msproject.myhome.mydays.main.event

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.msproject.myhome.mydays.main.event.adapter.CategoryRecyclerViewAdapter
import com.msproject.myhome.mydays.main.event.adapter.EventRecyclerViewAdapter
import com.msproject.myhome.mydays.model.Category
import com.msproject.myhome.mydays.model.ChartData
import com.msproject.myhome.mydays.model.Event
import com.msproject.myhome.mydays.model.EventItem
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

class EventViewModel @Inject constructor(private val eventRepository: EventRepository, private val categoryRepository: CategoryRepository):ViewModel() {
    val _eventList = MutableLiveData<List<EventItem>>(ArrayList())
    val _categoryList = MutableLiveData<List<Category>>()
    val _date = MutableLiveData<Date>()
    val finishEvent = SingleLiveEvent<Unit>()
    val contentDialogEvent = SingleLiveEvent<Unit>()
    val categoryDialogEvent = SingleLiveEvent<Unit>()

    val eventList: LiveData<List<EventItem>> get() = _eventList
    val categoryList: LiveData<List<Category>> get() = _categoryList
    val date: LiveData<Date> get() = _date
    val eventAdapter = MutableLiveData(EventRecyclerViewAdapter())
    val categoryAdapter = MutableLiveData(CategoryRecyclerViewAdapter())

    fun initBaseData(type:Int, dateString:String, owner: LifecycleOwner){
        //date + time 조합해서 repository에서 불러오기
        val format = SimpleDateFormat("yyyy-MM-dd")
        _date.value = format.parse(dateString)

        initEventList(type, owner)
        initCategoryList(owner)
    }

    fun initEventList(type:Int, owner: LifecycleOwner){
        if(date.value == null) return
        eventRepository.getEventList(date.value!!.time, type, type + 5).observe(owner, Observer {
            CoroutineScope(Dispatchers.IO).launch {
                val myEventList = ArrayList<EventItem>()
                for(i in 0..5){
                    val eventItem = Event()
                    eventItem.date = date.value?.time ?: 0
                    eventItem.time = type + i
                    myEventList.add(EventItem(eventItem, null))
                }
                for(item in it){
                    val category = categoryRepository.getCategoryById(item.cid)
                    myEventList[item.time - type] = EventItem(item, category)
                }
                _eventList.postValue(myEventList)
            }
        })
    }

    fun initCategoryList(owner:LifecycleOwner){
        categoryRepository.getCategoryList().observe(owner, Observer {
            _categoryList.postValue(it)
        })
    }

    fun submitEventList(){
        contentDialogEvent.call()
    }
    fun postEventList(content:String){
        CoroutineScope(Dispatchers.IO).launch {
            eventAdapter.value?.apply {
                for(item in eventList) {
                    val exist = eventRepository.exist(item.event.date, item.event.time)
                    if(exist == null){
                        if(item.category != null){
                            item.event.content = content
                            eventRepository.insertEvent(item.event)
                        }
                    }
                    else{
                        if(item.category == null){
                            eventRepository.deleteEventById(item.event.id)
                        }
                        else if(item.category?.id != exist.cid){
                            item.event.content = content
                            eventRepository.updateEvent(item.event)
                        }
                    }
                }
                clear()
                categoryAdapter.value?.clear()
            }

        }
    }

    fun onClickBackButton(){
        finishEvent.call()
    }

    fun addCategory(){
        categoryDialogEvent.call()
    }
}