package com.msproject.myhome.mydays.main.dailygraph.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.msproject.myhome.mydays.databinding.ItemGraphBinding
import com.msproject.myhome.mydays.model.ChartCategory

class DailyGraphRecyclerViewAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var items:List<ChartCategory> = ArrayList()
    class ViewHolder(private val binding:ItemGraphBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(chartCategory: ChartCategory){
            binding.model = chartCategory
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemGraphBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position])
    }
}