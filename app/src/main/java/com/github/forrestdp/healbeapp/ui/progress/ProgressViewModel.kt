package com.github.forrestdp.healbeapp.ui.progress

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.forrestdp.healbeapp.model.database.SportBeDatabaseDao
import com.github.mikephil.charting.data.*
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val database: SportBeDatabaseDao,
    application: Application,
) : AndroidViewModel(application) {
    private val _progressBarChartData = MutableLiveData<BarData>()
    val progressBarChartData: LiveData<BarData> = _progressBarChartData

    private val _progressLineChartData = MutableLiveData<LineData>()
    val progressLineChartData: LiveData<LineData> = _progressLineChartData

    private val _periodOverallDistance = MutableLiveData(15.4)
    private val _differenceFromLastPeriodOverallDistance = MutableLiveData(30)
    private val _cupText = MutableLiveData("Данных не хватает")
    private val _fireText = MutableLiveData("Данных не хватает")
    private val _stopwatchText = MutableLiveData("Данных не хватает")
    private val _lightningText = MutableLiveData("Данных не хвататет")

    init {
        viewModelScope.launch {

        }
    }
}