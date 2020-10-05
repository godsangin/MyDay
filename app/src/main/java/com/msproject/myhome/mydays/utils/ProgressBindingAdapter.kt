package com.msproject.myhome.mydays.utils

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.msproject.myhome.mydays.R
import com.msproject.myhome.mydays.model.ProgressItem

object ProgressBindingAdapter {
    @BindingAdapter("bind_progress_ratio")
    @JvmStatic
    fun bindProgressRatio(textView:TextView, item:ProgressItem?){
        if(item == null) return
        textView.text = (item.value.toDouble() * 100 / item.total).toInt().toString() + "%"
    }

    @BindingAdapter("bind_progress_image")
    @JvmStatic
    fun bindProgressImage(imageView: ImageView, item:ProgressItem?){
        if(item == null) return
        if((item.value.toDouble() * 100 / item.total).toInt() >= 70){
            Glide.with(imageView.context).load(R.drawable.gold).into(imageView)
        }
        else{
            imageView.setImageResource(R.drawable.silver)
        }
    }

    @BindingAdapter("bind_progress_text")
    @JvmStatic
    fun bindProgressText(textView: TextView, item:ProgressItem?){
        if(item == null) return
        textView.text = item.value.toString() + " / " + item.total
    }

    @BindingAdapter("bind_progress_bar")
    @JvmStatic
    fun bindProgressBar(progressBar: ProgressBar, item:ProgressItem?){
        if(item == null) return
        progressBar.progress = (item.value.toDouble() * 100 / item.total).toInt()
    }
}