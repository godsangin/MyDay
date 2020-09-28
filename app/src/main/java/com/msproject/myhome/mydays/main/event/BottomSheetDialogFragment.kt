package com.msproject.myhome.mydays.main.event

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.msproject.myhome.mydays.R
import com.msproject.myhome.mydays.databinding.DialogCategoryBinding

class BottomSheetDialogFragment(private val viewModelFactory: ViewModelProvider.Factory, private val activity:Activity, private val cid:Long) :BottomSheetDialogFragment(){
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val binding = DialogCategoryBinding.inflate(LayoutInflater.from(context))
        val categoryDialogViewModel = ViewModelProviders.of(this, viewModelFactory)[CategoryDialogViewModel::class.java]
        binding?.lifecycleOwner = this
        binding?.model = categoryDialogViewModel
        dialog.setContentView(binding.root)
        val owner = this
        categoryDialogViewModel.apply {
            getInitialCategory(cid)
            cancelEvent.observe(owner, Observer {
                dialog.dismiss()
            })
            submitEvent.observe(owner, Observer {
                snackbarEvent.postValue(2)
                dialog.dismiss()
            })
            snackbarEvent.observe(owner, Observer {
                when(it){
                    1 -> {
                        showSnackbar(getString(R.string.message_dialog_empty_category))
                    }
                    2 -> {
                        showSnackbar(getString(R.string.message_category_added))
                    }
                }
            })
        }
    }

    fun showSnackbar(message:String){
        Snackbar.make(activity.window.decorView.rootView, message, Snackbar.LENGTH_SHORT).show()
    }
}