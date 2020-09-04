package com.msproject.myhome.mydays.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.msproject.myhome.mydays.model.Event

@Dao
interface EventDao: BaseDao<Event>{
    @Query("SELECT * FROM event")
    fun getEventList():LiveData<List<Event>>
    @Query("SELECT * FROM event WHERE startDate BETWEEN :startDate AND :endDate OR endDate BETWEEN :startDate AND :endDate ORDER BY startDate")
    fun getEventList(startDate:Long, endDate:Long):LiveData<List<Event>>
    @Query("DELETE FROM event WHERE id =:id")
    fun deleteEventById(id:Long)
}