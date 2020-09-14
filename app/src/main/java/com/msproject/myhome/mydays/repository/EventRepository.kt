package com.msproject.myhome.mydays.repository

import androidx.lifecycle.LiveData
import com.msproject.myhome.mydays.model.Event

interface EventRepository {
    fun getEventList():LiveData<List<Event>>
    fun getEventList(date:Long):LiveData<List<Event>>
    fun getEventList(date:Long, startTime:Int, endTime:Int):LiveData<List<Event>>
    fun insertEvent(event: Event)
    fun updateEvent(event: Event)
    fun deleteEventById(id:Long)
}