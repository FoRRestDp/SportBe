package com.github.forrestdp.healbeapp.ui.progress

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.forrestdp.healbeapp.model.database.SportBeDatabaseDao
import com.github.mikephil.charting.data.*
import com.healbe.healbesdk.business_api.HealbeSdk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow

class ProgressViewModel(
    private val table: SportBeDatabaseDao,
    application: Application,
) : AndroidViewModel(application) {
    private val _progressBarChartData = MutableLiveData<BarData>()
    val progressBarChartData: LiveData<BarData> = _progressBarChartData

    private val _progressLineChartData = MutableLiveData<LineData>()
    val progressLineChartData: LiveData<LineData> = _progressLineChartData

    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            val workouts = table.getAllWorkouts()
//            val entries = workouts.map {
//                BarEntry(
//                    it.startTimestamp.toFloat(),
//                    (it.startTimestamp - it.endTimestamp).toFloat().div(60)
//                )
//            }
//            val dataSet = BarDataSet(entries, "Время тренировок")
//            _progressBarChartData.postValue(BarData(dataSet))
//
//            val calEntries = workouts.map {
//
//                val healthData = HealbeSdk.get().HEALTH_DATA
//                var cals = 0
//                healthData.getEnergySummary(it.startTimestamp, it.endTimestamp).asFlow().collect { list ->
//                    val kcal = list.map { it.get()?.activityKcal ?: 0 }.sum()
//                    cals = kcal
//                }
//
//                Entry(it.startTimestamp.toFloat(), cals.toFloat())
//            }
//            val calDataSet = LineDataSet(calEntries, "Расход калорий")
//            _progressLineChartData.postValue(LineData(calDataSet))
//        }
    }
}