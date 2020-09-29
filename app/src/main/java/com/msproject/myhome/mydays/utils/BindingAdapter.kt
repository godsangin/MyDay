package com.msproject.myhome.mydays.utils

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.msproject.myhome.mydays.main.event.adapter.CategoryRecyclerViewAdapter
import com.msproject.myhome.mydays.main.event.adapter.EventRecyclerViewAdapter
import com.msproject.myhome.mydays.model.Category
import com.msproject.myhome.mydays.model.ChartData
import com.msproject.myhome.mydays.model.EventItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object BindingAdapter {
    @BindingAdapter("bind_chart_item")
    @JvmStatic
    fun bindChartItem(chart: PieChart, items:List<ChartData>?){
        if(items == null) {
            val yValues = ArrayList<PieEntry>()
            yValues.add(PieEntry(1.toFloat(),"Empty"))
            val dataSet = PieDataSet(yValues, "Empty")
            dataSet.setColors(Color.WHITE, 0xff)
            val data = PieData(dataSet)
            data.setValueTextSize(10F)
            data.setValueTextColor(Color.BLACK)
            chart.data = data
            return
        }
        chart.setUsePercentValues(false)
        chart.description.isEnabled = false
        chart.setExtraOffsets(5F,10F,5F,5F)
        chart.dragDecelerationFrictionCoef = 0.95F
        chart.isDrawHoleEnabled = false
        chart.transparentCircleRadius = 61F
        chart.legend.isWordWrapEnabled = true

        val yValues = ArrayList<PieEntry>()
        val colorArray = ArrayList<Int>()

        for(item in items){
            val entry = PieEntry(item.size.toFloat(), item.eventName)
            yValues.add(entry)
            colorArray.add(Color.parseColor(item.color ?: "#ffffff"))
        }
        val dataSet = PieDataSet(yValues, "plan")
        dataSet.setColors(colorArray.toIntArray(), 0xff)
        dataSet.valueFormatter = MyIntValueFormatter()

        val data = PieData(dataSet)
        data.setValueTextSize(10F)
        data.setValueTextColor(Color.BLACK)

        chart.data = data
        chart.notifyDataSetChanged()
        chart.invalidate()
    }

    @BindingAdapter("bind_item")
    @JvmStatic
    fun bindEventItem(recyclerView:RecyclerView, items:List<EventItem>?){
        if(items == null) return
        if(recyclerView.adapter == null){
            recyclerView.adapter = EventRecyclerViewAdapter()
        }
        (recyclerView.adapter as EventRecyclerViewAdapter).eventList = items
        recyclerView.adapter?.notifyDataSetChanged()
    }
    @BindingAdapter("bind_item")
    @JvmStatic
    fun bindCategoryItem(recyclerView:RecyclerView, items:List<Category>?){
        if(items == null) return
        if(recyclerView.adapter == null){
            recyclerView.adapter = CategoryRecyclerViewAdapter()
            recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 2)
        }
        (recyclerView.adapter as CategoryRecyclerViewAdapter).categoryList = items
        recyclerView.adapter?.notifyDataSetChanged()
    }
    @BindingAdapter("bind_adapter")
    @JvmStatic
    fun bindEventAdapter(recyclerView: RecyclerView, adapter:EventRecyclerViewAdapter){
        recyclerView.adapter = adapter
    }
    @BindingAdapter("bind_adapter")
    @JvmStatic
    fun bindCategoryAdapter(recyclerView: RecyclerView, adapter:CategoryRecyclerViewAdapter){
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 2)
    }
    @BindingAdapter("bind_category")
    @JvmStatic
    fun bindCategory(recyclerView: RecyclerView, category:Category?){
        if(category == null) return
        (recyclerView.adapter as EventRecyclerViewAdapter).category = category
    }
    @BindingAdapter("bind_text")
    @JvmStatic
    fun bindTimeText(textView: TextView, timeType:Int){
        val text = if(timeType < 10){
            "0" + timeType + ":00"
        }else{
            timeType.toString() + ":00"
        }
        textView.text = text
    }

    @BindingAdapter("bind_text")
    @JvmStatic
    fun bindLongText(textView: TextView, date:Long?){
        if(date == null) return
        val format = SimpleDateFormat("yyyy-MM-dd")
        textView.text = format.format(Date(date))
    }

    @BindingAdapter("bind_color")
    @JvmStatic
    fun bindColor(layout:LinearLayout, color:String?){
        if(color == null){
            layout.setBackgroundColor(Color.parseColor("#ffffff"))
            return
        }
        layout.setBackgroundColor(Color.parseColor(color))
    }
    @BindingAdapter("bind_color")
    @JvmStatic
    fun bindColor(layout:ImageView, color:String?){
        if(color == null){
            layout.setBackgroundColor(Color.parseColor("#ffffff"))
            return
        }
        layout.setBackgroundColor(Color.parseColor(color))
    }

    @BindingAdapter("bind_date")
    @JvmStatic
    fun bindDate(textView: TextView, date:Date?){
        if(date == null) return
        val format = SimpleDateFormat("MM월dd일")
        textView.text = format.format(date)
    }

    @BindingAdapter("bind_color_select")
    @JvmStatic
    fun bindColorSelect(imageView: ImageView, color:String){

    }
}