package com.msproject.myhome.mydays.main.toolbar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msproject.myhome.mydays.utils.SingleLiveEvent
import javax.inject.Inject

class ToolbarViewModel @Inject constructor() :ViewModel(){
    val titleText = MutableLiveData<String>("")
    val backButtonEvent = SingleLiveEvent<Unit>()
    val drawerEvent = SingleLiveEvent<Unit>()

    fun onClickBackButton(){
        backButtonEvent.call()
    }

    fun onMenuClicked(){
        drawerEvent.call()
    }
}