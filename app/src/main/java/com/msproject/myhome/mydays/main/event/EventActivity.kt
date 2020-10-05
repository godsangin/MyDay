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
import com.google.android.material.snackbar.Snackbar
import com.msproject.myhome.mydays.R
import com.msproject.myhome.mydays.application.MyApplication
import com.msproject.myhome.mydays.base.BaseActivity
import com.msproject.myhome.mydays.databinding.ActivityEventBinding
import com.msproject.myhome.mydays.databinding.DialogCategoryBinding
import com.msproject.myhome.mydays.main.event.adapter.CategoryRecyclerViewAdapter
import javax.inject.Inject

class EventActivity : BaseActivity(){

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
            categoryDialogEvent.observe(owner, Observer {
                showCategoryDialog()
            })
            categoryLongClickEvent.observe(owner, Observer {
                showCategoryOptionsDialog(it)
            })
            categoryRemovedEvent.observe(owner, Observer {
                showSnackbar(getString(R.string.message_category_removed))
            })
        }
    }

    fun showCategoryDialog(cid:Long = (-1).toLong()){
        val bottomDialogFragment = BottomSheetDialogFragment(viewModelFactory, this, cid)
        bottomDialogFragment.show(supportFragmentManager, bottomDialogFragment.tag)
    }

    fun showCategoryOptionsDialog(cid:Long){
        val dialog = AlertDialog.Builder(this)
                .setItems(resources.getStringArray(R.array.category_dialog_options)){
                    _, which ->
                    when(which){
                        0 -> {
                            showCategoryDialog(cid)
                        }
                        1 -> {
                            showRemoveCategoryConfirmDialog(cid)
                        }
                    }
                }
        dialog.show()
    }

    fun showRemoveCategoryConfirmDialog(cid:Long){
        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_dialog_category_remove))
                .setMessage(getString(R.string.message_dialog_category_remove))
                .setPositiveButton(getString(R.string.text_submit)){
                    _,_->
                    eventViewModel.removeCategory(cid)
                }
                .setNegativeButton(getString(R.string.text_cancel)){
                    _,_->
                }
        dialog.show()
    }
    fun showSnackbar(message:String){
        Snackbar.make(window.decorView.rootView, message, Snackbar.LENGTH_SHORT).show()
    }
}