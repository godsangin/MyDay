package com.msproject.myhome.mydays.main.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.msproject.myhome.mydays.databinding.ItemCalendarBinding
import com.msproject.myhome.mydays.model.CalendarItem
import com.msproject.myhome.mydays.model.Category

class WeeklyCalendarRecyclerViewAdapter :RecyclerView.Adapter<WeeklyCalendarRecyclerViewAdapter.ViewHolder>(){
    var weeklyItemList:List<CalendarItem> = ArrayList()
    class ViewHolder(private val binding:ItemCalendarBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(categoryList:CalendarItem){
            binding.model = categoryList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weeklyItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(weeklyItemList[position])
    }
}