package com.msproject.myhome.mydays.main.event

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
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
    val categoryLongClickEvent = SingleLiveEvent<Long>()
    val categoryRemovedEvent = SingleLiveEvent<Unit>()

    val eventList: LiveData<List<EventItem>> get() = _eventList
    val categoryList: LiveData<List<Category>> get() = _categoryList
    val date: LiveData<Date> get() = _date
    var originList:List<EventItem> = ArrayList()
    val categoryAdapter = MutableLiveData(CategoryRecyclerViewAdapter())

    lateinit var initialEventListLiveData:LiveData<List<Event>>
    var type:Int= 0

    fun initBaseData(type:Int, dateString:String, owner: LifecycleOwner){
        //date + time 조합해서 repository에서 불러오기
        val format = SimpleDateFormat("yyyy-MM-dd")
        _date.value = format.parse(dateString)
        this.type = type
        initEventList(type, owner)
        initCategoryList(owner)
        initCategoryLongClickEvent(owner)
    }

    fun initEventList(type:Int, owner: LifecycleOwner){
        if(date.value == null) return
        initialEventListLiveData = eventRepository.getEventList(date.value!!.time, type, type + 5)
        initialEventListLiveData.observe(owner, Observer {
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
                originList = myEventList.clone() as List<EventItem>
                _eventList.postValue(myEventList)
            }
        })
    }

    fun initCategoryList(owner:LifecycleOwner){
        categoryRepository.getCategoryList().observe(owner, Observer {
            _categoryList.postValue(it)
        })
    }

    fun initCategoryLongClickEvent(owner:LifecycleOwner){
        categoryAdapter.value?.longClickEvent?.observe(owner, Observer {
            if(it == null) return@Observer
            categoryLongClickEvent.postValue(it)
            initialEventListLiveData.removeObservers(owner)
            initEventList(type, owner)
            categoryAdapter.value?.clear()
        })
    }

    fun submitEventList(){
        contentDialogEvent.call()
    }
    fun postEventList(content:String){
        CoroutineScope(Dispatchers.IO).launch {
            for(item in eventList.value ?: ArrayList()) {
                val exist = eventRepository.exist(item.event.date, item.event.time)
                if(exist == null){
                    if(item.category != null){
                        item.event.content = content
                        item.event.cid = item.category?.id ?: 0
                        eventRepository.insertEvent(item.event)
                    }
                }
                else{
                    if(item.category == null){
                        eventRepository.deleteEventById(item.event.id)
                    }
                    else if(item.category?.id != exist.cid){
                        item.event.content = content
                        item.event.cid = item.category?.id ?: 0
                        eventRepository.updateEvent(item.event)
                    }
                }
            }
            categoryAdapter.value?.clear()
        }
    }

    fun select(index:Int){
        val changed = eventList.value
        if(CategoryRecyclerViewAdapter.selectedCategory == null || CategoryRecyclerViewAdapter.selectedCategory.value?.id != changed?.get(index)?.category?.id){
            changed?.get(index)?.category = CategoryRecyclerViewAdapter.selectedCategory.value
        }
        else{
            changed?.get(index)?.category = null
        }
        _eventList.postValue(changed)
    }

    fun onClickBackButton(){
        finishEvent.call()
        categoryAdapter.value?.clear()
    }

    fun addCategory(){
        categoryDialogEvent.call()
    }

    fun removeCategory(cid:Long){
        CoroutineScope(Dispatchers.IO).launch {
            eventRepository.deleteEventByCid(cid)
            categoryRepository.deleteCategory(cid)
        }
        categoryRemovedEvent.call()
    }



}