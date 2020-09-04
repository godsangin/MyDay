package com.msproject.myhome.mydays.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.msproject.myhome.mydays.model.Category

@Dao
interface CategoryDao: BaseDao<Category>{
    @Query("SELECT * FROM category")
    fun getCategoryList():LiveData<List<Category>>
    @Query("SELECT * FROM category WHERE id =:id")
    fun getCategoryById(id:Long):Category
    @Query("DELETE FROM category WHERE id =:id")
    fun deleteCategoryById(id:Long)
}