package com.msproject.myhome.mydays.utils

import android.graphics.Color
import android.util.Log
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.msproject.myhome.mydays.model.ChartData

object BindingAdapter {
    @BindingAdapter("bind_chart_item")
    @JvmStatic
    fun bindChartItem(chart: PieChart, items:List<ChartData>?){
        if(items == null) {
            val yValues = ArrayList<PieEntry>()
            yValues.add(PieEntry(1.toFloat(),"Empty"))
            val dataSet = PieDataSet(yValues, "plan")
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
        chart.setHoleColor(Color.BLACK)
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

        val data = PieData(dataSet)
        data.setValueTextSize(10F)
        data.setValueTextColor(Color.BLACK)

        chart.data = data
    }
}