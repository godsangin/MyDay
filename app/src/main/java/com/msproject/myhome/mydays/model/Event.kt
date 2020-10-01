package com.msproject.myhome.mydays.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class Event(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0,
    var cid:Long = 0,
    var date:Long = 0,
    var time:Int = 0
)