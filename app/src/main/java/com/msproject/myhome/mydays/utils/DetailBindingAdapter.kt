package com.msproject.myhome.mydays.utils

import android.app.Activity
import android.graphics.Color
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.msproject.myhome.mydays.main.dailygraph.adapter.DailyGraphRecyclerViewAdapter
import com.msproject.myhome.mydays.main.fragment.adapter.DetailRecyclerViewAdapter
import com.msproject.myhome.mydays.model.Category
import com.msproject.myhome.mydays.model.ChartCategory
import com.msproject.myhome.mydays.model.EventItem
import kotlinx.android.synthetic.main.activity_daily_graph.*

object DetailBindingAdapter {
    @BindingAdapter(value = ["bind_chart_item", "bind_day"])
    @JvmStatic
    fun bindChartItem(chart: LineChart, items:List<Int>?, day:Int?){
        if(items == null || day == null) return

        val entries: MutableList<Entry> = ArrayList()
        for(i in items.indices){
            entries.add(Entry((i + day + 1).toFloat(), items[i].toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "계획시간")
        lineDataSet.lineWidth = 2f
        lineDataSet.circleRadius = 6f
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"))
        lineDataSet.circleHoleColor = Color.BLUE
        lineDataSet.color = Color.parseColor("#FFA1B4DC")
        lineDataSet.setDrawCircleHole(true)
        lineDataSet.setDrawCircles(true)
        lineDataSet.setDrawHorizontalHighlightIndicator(false)
        lineDataSet.setDrawHighlightIndicators(false)
        lineDataSet.setDrawValues(false)

        val lineData = LineData(lineDataSet)
        chart.setData(lineData)
        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.axisMaximum = 24f

        val xAxis: XAxis = chart.getXAxis()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = Color.BLACK
        xAxis.enableGridDashedLine(8f, 24f, 0f)
        xAxis.valueFormatter = MyDayValueFormatter()

        val yLAxis: YAxis = chart.getAxisLeft()
        yLAxis.textColor = Color.BLACK

        val yRAxis: YAxis = chart.getAxisRight()
        yRAxis.setDrawLabels(false)
        yRAxis.setDrawAxisLine(false)
        yRAxis.setDrawGridLines(false)

        val description = Description()
        description.setText("")

        chart.setDoubleTapToZoomEnabled(false)
        chart.setDrawGridBackground(false)
        chart.setDescription(description)
        chart.invalidate()
    }

    @BindingAdapter(value = ["bind_bar_chart_data", "bind_day"])
    @JvmStatic
    fun bindChartItem(chart:BarChart, items:List<Int>?, day:Int?){
        if(items == null || day == null) return
        val entries: MutableList<BarEntry> = ArrayList()
        for(i in items.indices){
            entries.add(BarEntry((i + day + 1).toFloat(), items[i].toFloat()))
        }

        val BarDataSet = BarDataSet(entries, "계획시간")
        BarDataSet.color = Color.parseColor("#FFA1B4DC")
        BarDataSet.setDrawValues(false)

        val barData = BarData(BarDataSet)
        chart.data = barData
        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.axisMaximum = 24f

        val xAxis: XAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = Color.BLACK
        xAxis.valueFormatter = MyDayValueFormatter()
        xAxis.enableGridDashedLine(8f, 24f, 0f)
        xAxis.granularity = 1f

        val yLAxis: YAxis = chart.axisLeft
        yLAxis.textColor = Color.BLACK

        val yRAxis: YAxis = chart.axisRight
        yRAxis.setDrawLabels(false)
        yRAxis.setDrawAxisLine(false)
        yRAxis.setDrawGridLines(false)

        val description = Description()
        description.text = ""

        chart.isDoubleTapToZoomEnabled = false
        chart.setDrawGridBackground(false)
        chart.description = description
        chart.notifyDataSetChanged()
        chart.invalidate()
    }

    @BindingAdapter("bind_pie_chart_item")
    @JvmStatic
    fun bindPieChartItem(chart: PieChart, items:List<Pair<Category, Int>>?){
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
        chart.dragDecelerationFrictionCoef = 0.95F
        chart.isDrawHoleEnabled = false
        chart.legend.isWordWrapEnabled = false

        val yValues = ArrayList<PieEntry>()
        val colorArray = ArrayList<Int>()
        var total = 0
        for(item in items){
            total += item.second
            val entry = PieEntry(item.second.toFloat(), item.first.name)
            yValues.add(entry)
            colorArray.add(Color.parseColor(item.first.color ?: "#ffffff"))
        }
        val dataSet = PieDataSet(yValues, "plan")
        dataSet.setColors(colorArray.toIntArray(), 0xff)
        dataSet.valueFormatter = MyPercentValueFormatter(total)
        val data = PieData(dataSet)
        data.setValueTextSize(10F)
        data.setValueTextColor(Color.WHITE)

        chart.data = data
        chart.notifyDataSetChanged()
        chart.invalidate()
    }

    @BindingAdapter("bind_bar_chart_item")
    @JvmStatic
    fun bindBarChartItem(barChart: BarChart, items:List<Pair<Category, Int>>?){
        if(items == null) return
        val entries: MutableList<BarEntry> = ArrayList()
        val sortItems = items.sortedBy {
            -it.second
        }
        val colorArray = ArrayList<Int>()
        val xAxisValueLabels = ArrayList<String>()
        for(i in sortItems.indices){
            entries.add(BarEntry(i.toFloat(), sortItems[i].second.toFloat()))
            colorArray.add(Color.parseColor(sortItems[i].first.color ?: "#ffffff"))
            xAxisValueLabels.add(sortItems[i].first.name)
        }

        val barDataSet = BarDataSet(entries, "plan")
        barDataSet.setColors(colorArray.toIntArray(), 0xff)
        barDataSet.setDrawValues(false)

        val barData = BarData(barDataSet)
        barChart.data = barData
        barChart.axisLeft.axisMinimum = 0f
//        barChart.xAxis.valueFormatter = IndexAxisValueFormatter()

        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = Color.BLACK
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValueLabels)
        xAxis.enableGridDashedLine(8f, 24f, 0f)
        xAxis.granularity = 1f

        val yLAxis: YAxis = barChart.axisLeft
        yLAxis.textColor = Color.BLACK

        val yRAxis: YAxis = barChart.axisRight
        yRAxis.setDrawLabels(false)
        yRAxis.setDrawAxisLine(false)
        yRAxis.setDrawGridLines(false)

        val description = Description()
        description.setText("")

        barChart.isDoubleTapToZoomEnabled = false
        barChart.setDrawGridBackground(false)
        barChart.description = description
        barChart.notifyDataSetChanged()
        barChart.invalidate()
    }

    @BindingAdapter(value = ["bind_line_chart_item", "bind_pie_chart_item", "bind_activity", "bind_day"])
    @JvmStatic
    fun bindDetailItems(recyclerView:RecyclerView, items:List<Int>?, citems:List<Pair<Category, Int>>?, activity: Activity?, day:Int?){
        if(items == null || citems == null || day == null || activity == null) return
        if(recyclerView.adapter == null){
            recyclerView.adapter = DetailRecyclerViewAdapter()
            recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 2)

        }
        val list = ArrayList<Pair<Int, List<Any>>>()
        list.add(Pair(0, items))
        list.add(Pair(1, citems))
        list.add(Pair(0, items))
        (recyclerView.adapter as DetailRecyclerViewAdapter).items = list
        (recyclerView.adapter as DetailRecyclerViewAdapter).activity = activity
        (recyclerView.adapter as DetailRecyclerViewAdapter).day = day
        recyclerView.adapter?.notifyDataSetChanged()
    }

    @BindingAdapter("bind_items")
    @JvmStatic
    fun bindItems(recyclerView: RecyclerView, items:List<ChartCategory>?){
        if(items == null) return
        if(recyclerView.adapter == null){
            recyclerView.adapter = DailyGraphRecyclerViewAdapter()
            recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 2)
        }
        (recyclerView.adapter as DailyGraphRecyclerViewAdapter).items = items
        recyclerView.adapter?.notifyDataSetChanged()
    }
}