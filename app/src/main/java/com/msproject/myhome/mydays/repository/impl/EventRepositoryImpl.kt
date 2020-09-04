package com.msproject.myhome.mydays.repository.impl

import androidx.lifecycle.LiveData
import com.msproject.myhome.mydays.model.Event
import com.msproject.myhome.mydays.repository.EventRepository
import com.msproject.myhome.mydays.repository.room.AppDatabase
import com.msproject.myhome.mydays.repository.room.EventDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(private val appDatabase: AppDatabase):EventRepository {
    fun getEventDao():EventDao{
        return appDatabase.getEventDao()
    }
    override fun getEventList():LiveData<List<Event>>{
        return getEventDao().getEventList()
    }
    override fun getEventList(startDate:Long, endDate:Long):LiveData<List<Event>>{
        return getEventDao().getEventList(startDate, endDate)
    }
    override fun insertEvent(event: Event){
        getEventDao().insert(event)
    }
    override fun updateEvent(event: Event){
        getEventDao().update(event)
    }

    override fun deleteEventById(id:Long){
        getEventDao().deleteEventById(id)
    }
}