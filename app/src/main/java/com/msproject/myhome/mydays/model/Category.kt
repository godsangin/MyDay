package com.msproject.myhome.mydays.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0,
    var name:String = "",
    var color:String = ""
)