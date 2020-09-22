package com.msproject.myhome.mydays.main.event

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.msproject.myhome.mydays.R
import com.msproject.myhome.mydays.application.MyApplication
import com.msproject.myhome.mydays.databinding.ActivityEventBinding
import com.msproject.myhome.mydays.databinding.DialogCategoryBinding
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
        val owner = this
        eventViewModel.apply {
            initBaseData(type, date, owner)
            finishEvent.observe(owner, Observer {
                finish()
            })
            contentDialogEvent.observe(owner, Observer {
                showInputDialog()
            })
            categoryDialogEvent.observe(owner, Observer {
                showCategoryDialog()
            })
        }
    }

    fun showInputDialog(){
        val inputView = EditText(this)
        val builder = AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_input_event))
                .setMessage(getString(R.string.message_input_event))
                .setView(inputView)
                .setPositiveButton(getString(R.string.text_submit)) { _, _ ->
                    eventViewModel.postEventList(inputView.text.toString())
                }
                .setNegativeButton(getString(R.string.text_cancel)) { _,_ ->

                }
        builder.show()
    }
    fun showCategoryDialog(){
        val bottomDialogFragment = BottomSheetDialogFragment(viewModelFactory, this)
        bottomDialogFragment.show(supportFragmentManager, bottomDialogFragment.tag)
    }
}