package com.msproject.myhome.mydays.main.fragment.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.msproject.myhome.mydays.databinding.ItemPlanCategoryBinding
import com.msproject.myhome.mydays.databinding.ItemPlanTimeBinding
import com.msproject.myhome.mydays.main.dailygraph.DailyGraphActivity
import com.msproject.myhome.mydays.main.statistic.StatisticActivity
import com.msproject.myhome.mydays.model.Category
import kotlinx.android.synthetic.main.item_plan_category.view.*
import kotlinx.android.synthetic.main.item_plan_time.view.*
import kotlinx.android.synthetic.main.item_plan_time.view.chart

class DetailRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var items = ArrayList<Pair<Int, List<Any>>>()
    var activity: Activity? = null
    var day:Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            0 -> {
                val binding = ItemPlanTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                binding.lifecycleOwner = parent.context as LifecycleOwner
                return DailyItemViewHolder(binding)
            }
            1 -> {
                val binding = ItemPlanCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                binding.lifecycleOwner = parent.context as LifecycleOwner
                return CategoryTimeViewHolder(binding)
            }
        }
        val binding = ItemPlanCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.lifecycleOwner = parent.context as LifecycleOwner
        return CategoryTimeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].first
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(position){
            0 -> {
                (holder as DailyItemViewHolder).bind(items[position].second as List<Int>, activity!!, day)
            }
            1 -> {
                (holder as CategoryTimeViewHolder).bind(items[position].second as List<Pair<Category, Int>>, activity!!)
            }
        }


    }

    class DailyItemViewHolder(private val binding: ItemPlanTimeBinding):RecyclerView.ViewHolder(binding.root){
        val viewModel = DetailRecyclerItemViewModel()
        fun bind(items:List<Int>, activity:Activity, day:Int){
            viewModel._dailyTime.postValue(items)
            viewModel.day.postValue(day)
            binding.model = viewModel
            viewModel.startActivityEvent.observe(binding.lifecycleOwner!!, Observer {
                if(activity == null) return@Observer
                val context = binding.root.context
                val intent = Intent(context, DailyGraphActivity::class.java)
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, binding.root.chart, binding.root.chart.transitionName)
                context.startActivity(intent, optionsCompat.toBundle())
            })
        }
    }

    class CategoryTimeViewHolder(private val binding:ItemPlanCategoryBinding):RecyclerView.ViewHolder(binding.root){
        val viewModel = DetailRecyclerItemViewModel()
        fun bind(items:List<Pair<Category, Int>>, activity:Activity){
            viewModel._chartCategory.postValue(items)
            binding.model = viewModel
            viewModel.startActivityEvent.observe(binding.lifecycleOwner!!, Observer {
                if(activity == null) return@Observer
                val context = binding.root.context
                val intent = Intent(context, StatisticActivity::class.java)
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, binding.root.piechart, binding.root.piechart.transitionName)
                context.startActivity(intent, optionsCompat.toBundle())
            })
            binding.root.piechart.setOnChartValueSelectedListener(object:OnChartValueSelectedListener{
                override fun onNothingSelected() {
                    viewModel.startActivityEvent.call()
                }

                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    viewModel.startActivityEvent.call()
                }
            })
        }
    }
}