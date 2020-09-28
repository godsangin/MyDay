package com.msproject.myhome.mydays.repository.impl

import androidx.lifecycle.LiveData
import com.msproject.myhome.mydays.model.Category
import com.msproject.myhome.mydays.repository.CategoryRepository
import com.msproject.myhome.mydays.repository.room.AppDatabase
import com.msproject.myhome.mydays.repository.room.CategoryDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(private val appDatabase: AppDatabase):CategoryRepository {
    fun getCategoryDao():CategoryDao{
        return appDatabase.getCategoryDao()
    }
    override fun getCategoryList(): LiveData<List<Category>> {
        return getCategoryDao().getCategoryList()
    }

    override fun getCategoryById(id: Long): Category? {
        return getCategoryDao().getCategoryById(id)
    }

    override fun insertCategory(category: Category) {
        getCategoryDao().insert(category)
    }

    override fun updateCategory(category: Category) {
        getCategoryDao().update(category)
    }

    override fun deleteCategory(id: Long) {
        getCategoryDao().deleteCategoryById(id)
    }
}