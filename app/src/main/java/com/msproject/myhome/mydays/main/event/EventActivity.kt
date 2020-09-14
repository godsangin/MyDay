package com.msproject.myhome.mydays.main.event

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.msproject.myhome.mydays.R
import com.msproject.myhome.mydays.application.MyApplication
import com.msproject.myhome.mydays.databinding.ActivityEventBinding
import com.msproject.myhome.mydays.main.event.adapter.CategoryRecyclerViewAdapter
import javax.inject.Inject

class EventActivity :AppCompatActivity(){

    lateinit var eventViewModel: EventViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.inject(this)
        val date = intent.getStringExtra("date") ?: ""
        val type = intent.getIntExtra("type", 0)
        eventViewModel = ViewModelProviders.of(this, viewModelFactory)[EventViewModel::class.java]
        val binding = DataBindingUtil.setContentView<ActivityEventBinding>(this, R.layout.activity_event)
        binding.lifecycleOwner = this
        binding.model = eventViewModel
        eventViewModel.initBaseData(type, date, this)
        eventViewModel.finishEvent.observe(this, Observer {
            finish()
        })
    }
}