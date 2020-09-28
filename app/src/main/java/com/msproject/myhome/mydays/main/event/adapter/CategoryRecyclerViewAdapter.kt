package com.msproject.myhome.mydays.main.event.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.msproject.myhome.mydays.R
import com.msproject.myhome.mydays.databinding.ItemCategoryBinding
import com.msproject.myhome.mydays.model.Category

class CategoryRecyclerViewAdapter :RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>(){
    var categoryList:List<Category> = ArrayList()
    var longClickEvent = MutableLiveData<Long>()
    companion object{
        var selectedCategory = MutableLiveData<Category>()
        var selectedView:View? = null
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position], longClickEvent)
    }
    fun clear(){
        selectedCategory.postValue(null)
        selectedView?.setBackgroundColor(Color.WHITE)
        selectedView = null


    }
    class ViewHolder(val binding: ItemCategoryBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(category:Category, longClickEvent:MutableLiveData<Long>){
            binding.model = category
            binding.root.setOnClickListener {
                if(selectedCategory.value?.equals(category) ?: false){
                    selectedView?.setBackgroundColor(it.context.getColor(R.color.colorWhite))
                    selectedCategory.postValue(null)
                    selectedView = null
                }
                else{
                    selectedCategory.postValue(category)
                    selectedView?.setBackgroundColor(it.context.getColor(R.color.colorWhite))
                    it.setBackgroundColor(it.context.getColor(R.color.colorAccent))
                    selectedView = it
                }
            }
            binding.root.setOnLongClickListener {
                longClickEvent.postValue(category.id)
                true
            }
        }
    }
}