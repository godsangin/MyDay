package com.msproject.myhome.mydays.main.event.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.msproject.myhome.mydays.databinding.EventItemBinding
import com.msproject.myhome.mydays.model.Category
import com.msproject.myhome.mydays.model.Event
import com.msproject.myhome.mydays.model.EventItem

class EventRecyclerViewAdapter :RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>(){
    var eventList:List<EventItem> = ArrayList()
    var category:Category? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(eventList[position])
        holder.binding.root.setOnClickListener {
            val view = it
            category?.let {
                val eventItem = eventList[position].event
                val event = Event(eventItem.id, it.id, eventItem.date, eventItem.time, "")
                if(eventList[position].category == null){
                    eventList[position].category = category
                    eventList[position].event = event
                }
                else if(eventList[position].category?.id != category?.id){
                    eventList[position].category = category
                    eventList[position].event = event
                }
                else{
                    eventList[position].category = null
                }
                notifyDataSetChanged()
            }
        }
    }

    fun clear(){
        category = null
    }

    class ViewHolder(val binding:EventItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:EventItem){
            binding.model = item
        }
    }
}