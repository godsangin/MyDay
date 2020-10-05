package com.msproject.myhome.mydays.utils

import com.github.mikephil.charting.formatter.ValueFormatter

class MyPercentValueFormatter(private val totalValue:Int) :ValueFormatter(){
    override fun getFormattedValue(value: Float): String {
        return (value * 100 / totalValue).toInt().toString() + "%"
    }
}