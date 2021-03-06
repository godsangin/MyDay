package com.msproject.myhome.mydays.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.NumberFormat

class MyIntValueFormatter : ValueFormatter() {
    private val mFormat = NumberFormat.getInstance()
    override fun getFormattedValue(value: Float): String {
        return value.toInt().toString()
    }
}