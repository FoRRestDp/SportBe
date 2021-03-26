package com.github.forrestdp.healbeapp.ui.progress

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.forrestdp.healbeapp.model.database.timestamps.WorkoutTimeBoundariesDao
import com.github.mikephil.charting.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val table: WorkoutTimeBoundariesDao,
    application: Application,
) : AndroidViewModel(application) {
    private val _progressBarChartData = MutableLiveData<BarData>()
    val progressBarChartData: LiveData<BarData> = _progressBarChartData

    private val _progressLineChartData = MutableLiveData<LineData>()
    val progressLineChartData: LiveData<LineData> = _progressLineChartData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val workouts = table.getAllWorkouts()
            val entries = workouts.map { BarEntry(it.startTimestamp.toFloat(), (it.startTimestamp - it.endTimestamp).toFloat().div(60)) }
            val dataSet = BarDataSet(entries, "Время тренировок")
            _progressBarChartData.postValue(BarData(dataSet))

            val calEntries = workouts.map { Entry(it.startTimestamp.toFloat(), it.calories.toFloat()) }
            val calDataSet = LineDataSet(calEntries, "Расход калорий")
            _progressLineChartData.postValue(LineData(calDataSet))
        }
    }
}