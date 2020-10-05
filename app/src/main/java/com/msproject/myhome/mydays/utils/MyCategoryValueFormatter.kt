package com.msproject.myhome.mydays.utils

import android.util.Log
import com.github.mikephil.charting.formatter.ValueFormatter
import com.msproject.myhome.mydays.model.Category

class MyCategoryValueFormatter(private val items:List<Pair<Category, Int>>):ValueFormatter(){
    override fun getFormattedValue(value: Float): String {
        return items[value.toInt()].first.name
    }
}