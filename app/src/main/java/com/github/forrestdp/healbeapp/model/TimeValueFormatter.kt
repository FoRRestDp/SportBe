package com.github.forrestdp.healbeapp.model

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*

class TimeValueFormatter : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val timestampSecs = value.toInt() / 1000
        val hours = timestampSecs / 3600
        val minutes = (timestampSecs % 3600) / 60
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
    }
}