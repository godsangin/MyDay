package com.msproject.myhome.mydays.utils

import com.github.mikephil.charting.formatter.ValueFormatter

class MyNullValueFormatter :ValueFormatter(){
    override fun getFormattedValue(value: Float): String {
        return ""
    }
}