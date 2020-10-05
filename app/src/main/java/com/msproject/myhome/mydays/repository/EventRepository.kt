package com.msproject.myhome.mydays.repository

import androidx.lifecycle.LiveData
import com.msproject.myhome.mydays.model.Event

interface EventRepository {
    fun getEventList():LiveData<List<Event>>
    fun getEventListCount():LiveData<Int>
    fun getEventList(date:Long):LiveData<List<Event>>
    fun getEventListSync(date:Long):List<Event>
    fun getEventList(startDate:Long, endDate:Long):LiveData<List<Event>>
    fun getEventListSync(startDate: Long, endDate: Long):List<Event>
    fun getEventListCount(startDate:Long, endDate:Long):LiveData<Int>
    fun getEventList(date:Long, startTime:Int, endTime:Int):LiveData<List<Event>>
    fun exist(date:Long, time:Int):Event
    fun insertEvent(event: Event)
    fun updateEvent(event: Event)
    fun deleteEventById(id:Long)
    fun deleteEventByCid(cid:Long)
}