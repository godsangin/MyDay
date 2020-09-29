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
    override fun getEventList(date:Long):LiveData<List<Event>>{
        return getEventDao().getEventList(date)
    }

    override fun getEventListSync(date: Long): List<Event> {
        return getEventDao().getEventListSync(date)
    }
    override fun getEventList(startDate: Long, endDate: Long): LiveData<List<Event>> {
        return getEventDao().getEventList(startDate, endDate)
    }

    override fun getEventListSync(startDate: Long, endDate: Long): List<Event> {
        return getEventDao().getEventListSync(startDate, endDate)
    }
    override fun getEventList(date: Long, startTime: Int, endTime: Int): LiveData<List<Event>> {
        return getEventDao().getEventList(date, startTime, endTime)
    }

    override fun exist(date: Long, time: Int): Event {
        return getEventDao().getEvent(date, time)
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

    override fun deleteEventByCid(cid: Long) {
        getEventDao().deleteEventByCid(cid)
    }
}