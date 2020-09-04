package com.msproject.myhome.mydays.repository

import androidx.lifecycle.LiveData
import com.msproject.myhome.mydays.model.Category

interface CategoryRepository{
    fun getCategoryList():LiveData<List<Category>>
    fun getCategoryById(id:Long):Category
    fun insertCategory(category: Category)
    fun updateCategory(category: Category)
    fun deleteCategory(id:Long)
}