package com.msproject.myhome.mydays.main.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.msproject.myhome.mydays.databinding.ItemCalendarCategoryBinding
import com.msproject.myhome.mydays.databinding.ItemCategoryBinding
import com.msproject.myhome.mydays.model.Category

class CalendarRecyclerViewAdapter:RecyclerView.Adapter<CalendarRecyclerViewAdapter.ViewHolder>(){
    var categoryList:List<Category> = ArrayList()
    class ViewHolder(private val binding:ItemCalendarCategoryBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(category:Category){
            binding.model = category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }
}