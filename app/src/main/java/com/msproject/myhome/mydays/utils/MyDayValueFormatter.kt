package com.msproject.myhome.mydays.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.NumberFormat

class MyDayValueFormatter : ValueFormatter() {
    private val mFormat = NumberFormat.getInstance()
    override fun getFormattedValue(value: Float): String {
        when(value.toInt() % 7){
            0 -> {
                return "일"
            }
            1 -> {
                return "월"
            }
            2 -> {
                return "화"
            }
            3 -> {
                return "수"
            }
            4 -> {
                return "목"
            }
            5 -> {
                return "금"
            }
            6 -> {
                return "토"
            }
        }
        return value.toInt().toString()
    }
}