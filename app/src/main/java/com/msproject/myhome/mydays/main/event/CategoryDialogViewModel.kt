package com.msproject.myhome.mydays.main.event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msproject.myhome.mydays.model.Category
import com.msproject.myhome.mydays.repository.CategoryRepository
import com.msproject.myhome.mydays.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import javax.inject.Inject

class CategoryDialogViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :ViewModel(){
    val cancelEvent = SingleLiveEvent<Unit>()
    val submitEvent = SingleLiveEvent<Unit>()
    val snackbarEvent =  SingleLiveEvent<Int>()
    val _selectedColor = MutableLiveData<String>("")
    val _categoryName = MutableLiveData<String>("")
    val _category = MutableLiveData<Category>()

    val selectedColor:LiveData<String> get() = _selectedColor
    val categoryName:LiveData<String> get() = _categoryName
    val category:LiveData<Category> get() = _category

    fun getInitialCategory(cid:Long){
        CoroutineScope(Dispatchers.IO).launch {
            val exist = categoryRepository.getCategoryById(cid)
            _category.postValue(exist)
            _categoryName.postValue(exist?.name ?: "")
        }
    }

    fun setSelectedColor(color:String){
        _selectedColor.postValue(color)
    }
    fun cancel(){
        cancelEvent.call()
    }

    fun submit(){
        if(selectedColor.value == "" || categoryName.value == ""){
            snackbarEvent.postValue(1)
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            val newCategory = category.value
            if(newCategory == null){
                categoryRepository.insertCategory(Category(0, categoryName?.value ?: "", selectedColor?.value ?: ""))
            }
            else{
                newCategory?.name = categoryName?.value ?: ""
                newCategory?.color = selectedColor?.value ?: ""
                categoryRepository.updateCategory(newCategory)
            }
        }
        submitEvent.call()
    }
}