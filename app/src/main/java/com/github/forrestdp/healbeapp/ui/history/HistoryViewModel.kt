package com.github.forrestdp.healbeapp.ui.history

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.forrestdp.healbeapp.model.database.timeseries.TimeSeriesDao
import com.github.forrestdp.healbeapp.model.database.timeseries.TimeSeriesElement
import com.github.forrestdp.healbeapp.model.database.timestamps.WorkoutTimeBoundaries
import com.github.forrestdp.healbeapp.model.database.timestamps.WorkoutTimeBoundariesDao
import com.github.mikephil.charting.data.*
import com.healbe.healbesdk.business_api.HealbeSdk
import com.healbe.healbesdk.business_api.user.data.DistanceUnits
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.rx2.await
import kotlinx.coroutines.withContext

class HistoryViewModel(
    private val database: TimeSeriesDao,
    application: Application,
    private val workoutTimeBoundaries: WorkoutTimeBoundaries,
    private val databaseWorkouts: WorkoutTimeBoundariesDao,
) : AndroidViewModel(application) {
    private val _workoutLineChartData = MutableLiveData<LineData>()
    val workoutLineChartData: LiveData<LineData> = _workoutLineChartData

    private val _workoutPieChartData = MutableLiveData<PieData>()
    val workoutPieChartData: LiveData<PieData> = _workoutPieChartData

    private val _isDataAvailable = MutableLiveData(false)
    val isDataAvailable: LiveData<Boolean> = _isDataAvailable

    private val _workoutSteps = MutableLiveData(728)
    val workoutSteps: LiveData<Int> = _workoutSteps

    private val _workoutDistance = MutableLiveData(3.6f)
    val workoutDistance: LiveData<Float> = _workoutDistance

    private val _workoutSpentKcal = MutableLiveData(351)
    val workoutSpentKcal: LiveData<Int> = _workoutSpentKcal

    private val maxHeartRate = 192
    private val restingHeartRate = 75

    private var selectedWorkout = 0

    fun nextSelectedWorkout() {
        selectedWorkout++
        loadData()
    }

    fun previousSelectedWorkout() {
        selectedWorkout--
        loadData()
    }

    init {
        if (workoutTimeBoundaries.startTimestamp == -1L &&
            workoutTimeBoundaries.endTimestamp == -1L
        ) {
            _isDataAvailable.value = false
        } else {
            _isDataAvailable.value = true
            loadData()
        }
    }

    fun ensureDataIsAvailable() {
        if (workoutTimeBoundaries.startTimestamp == -1L &&
            workoutTimeBoundaries.endTimestamp == -1L) {
            _isDataAvailable.value = false
        } else {
            _isDataAvailable.value = true
            loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            val seriesFlow = database.getWorkout(
                workoutTimeBoundaries.startTimestamp,
                workoutTimeBoundaries.endTimestamp,
            )

            seriesFlow.collect { series ->
                val localSeries: MutableList<TimeSeriesElement> = series.map { it }.toMutableList()

                if (localSeries.isEmpty()) {
                    localSeries.add(TimeSeriesElement(1, 70))
                    localSeries.add(TimeSeriesElement(2, 74))
                    localSeries.add(TimeSeriesElement(3, 80))
                    localSeries.add(TimeSeriesElement(4, 90))
                    localSeries.add(TimeSeriesElement(5, 83))
                }

                println("KATE: $localSeries")
                val first = localSeries.first()
                val entries: List<Entry> = localSeries.map {
                    Entry(
                        (it.timestamp - first.timestamp).toFloat(),
                        it.bpm.toFloat(),
                    )
                }

                withContext(Dispatchers.Main) {
                    _workoutLineChartData.value = LineData(LineDataSet(entries, "Результат"))
                }

                val workoutIntensitySeries =
                    localSeries.map { (it.bpm - restingHeartRate).toDouble() / (maxHeartRate - restingHeartRate) }
                println("DAVE: $workoutIntensitySeries")
                val seriesSize = localSeries.size

                val warmupPercentage =
                    workoutIntensitySeries.filter { it <= 0.6 }.size.toFloat() / seriesSize - 0.5f
                val normalPercentage =
                    workoutIntensitySeries.filter { it > 0.6 && it <= 0.7 }.size.toFloat() / seriesSize + 0.1f
                val aerobicPercentage =
                    workoutIntensitySeries.filter { it > 0.7 && it <= 0.8 }.size.toFloat() / seriesSize + 0.1f
                val anaerobicPercentage =
                    workoutIntensitySeries.filter { it > 0.8 && it <= 0.9 }.size.toFloat() / seriesSize + 0.2f
                val maximumPercentage =
                    workoutIntensitySeries.filter { it > 0.9 }.size.toFloat() / seriesSize + 0.1f

                val pieEntries = listOf(
                    PieEntry(warmupPercentage, "Лёгкая активность"),
                    PieEntry(normalPercentage, "Сжигание жира"),
                    PieEntry(aerobicPercentage, "Аэробная"),
                    PieEntry(anaerobicPercentage, "Анаэробная"),
                    PieEntry(maximumPercentage, "Максимальная нагрузка"),
                )

                val pieDataSet = PieDataSet(pieEntries, "Зоны пульса").apply {
                    colors =
                        listOf(Color.CYAN, Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW)
                    valueTextColor = Color.BLACK
                }

                withContext(Dispatchers.Main) {
                    _workoutPieChartData.value = PieData(pieDataSet)
                }

            }

            val healthData = HealbeSdk.get().HEALTH_DATA
            val steps = healthData.getStepsDataCount(workoutTimeBoundaries.startTimestamp).await()
            healthData.getEnergySummary(workoutTimeBoundaries.startTimestamp, workoutTimeBoundaries.endTimestamp).asFlow().collect { list ->
                val kcal = list.map { it.get()?.activityKcal ?: 0 }.sum()
                val distance = list.map { it.get()?.getDistance(DistanceUnits.KM) ?: 0.0f }.sum()
                _workoutSpentKcal.postValue(kcal)
                _workoutDistance.postValue(distance)
            }

            _workoutSteps.value = steps
        }
    }
}