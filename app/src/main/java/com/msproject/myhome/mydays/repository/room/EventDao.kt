package com.msproject.myhome.mydays.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.msproject.myhome.mydays.model.Event

@Dao
interface EventDao: BaseDao<Event>{
    @Query("SELECT * FROM event")
    fun getEventList():LiveData<List<Event>>
    @Query("SELECT COUNT(*) FROM event")
    fun getEventListCount():LiveData<Int>
    @Query("SELECT * FROM event WHERE date IS :date ORDER BY time")
    fun getEventList(date:Long):LiveData<List<Event>>
    @Query("SELECT * FROM event WHERE date IS :date ORDER BY time")
    fun getEventListSync(date:Long):List<Event>
    @Query("SELECT * FROM event WHERE date BETWEEN :startDate AND :endDate")
    fun getEventList(startDate:Long, endDate:Long):LiveData<List<Event>>
    @Query("SELECT * FROM event WHERE date BETWEEN :startDate AND :endDate")
    fun getEventListSync(startDate:Long, endDate:Long):List<Event>
    @Query("SELECT COUNT(*) FROM event WHERE date BETWEEN :startDate AND :endDate")
    fun getEventListCount(startDate:Long, endDate:Long):LiveData<Int>
    @Query("SELECT * FROM event WHERE date IS :date AND time BETWEEN :startTime AND :endTime ORDER BY time")
    fun getEventList(date:Long, startTime:Int, endTime:Int):LiveData<List<Event>>
    @Query("SELECT * FROM event WHERE date IS :date AND time IS :time")
    fun getEvent(date:Long, time:Int):Event
    @Query("DELETE FROM event WHERE id =:id")
    fun deleteEventById(id:Long)
    @Query("DELETE FROM event WHERE cid=:cid")
    fun deleteEventByCid(cid:Long)
}