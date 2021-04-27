package com.github.forrestdp.healbeapp.ui.history

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.*
import com.github.forrestdp.healbeapp.model.database.SportBeDatabaseDao
import com.github.forrestdp.healbeapp.model.database.entities.HeartRateSeriesElement
import com.github.forrestdp.healbeapp.model.database.entities.Workout
import com.github.mikephil.charting.data.*
import com.healbe.healbesdk.business_api.HealbeSdk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.math.roundToInt

class HistoryViewModel(
    private val database: SportBeDatabaseDao,
    private val justFinishedWorkoutId: Long,
    application: Application,
) : AndroidViewModel(application) {
    private val _workoutLineChartData = MutableLiveData<LineData>()
    val workoutLineChartData: LiveData<LineData> = _workoutLineChartData

    private val _workoutPieChartData = MutableLiveData<PieData>()
    val workoutPieChartData: LiveData<PieData> = _workoutPieChartData

    private val _isNoDataAvailable = MutableLiveData<Boolean>()
    val isNoDataAvailable: LiveData<Boolean> = _isNoDataAvailable

    private val _workoutDistanceKM = MutableLiveData(-1f)
    val workoutDistanceKM: LiveData<Float> = _workoutDistanceKM

    private val _workoutDurationSecs = MutableLiveData<Long>(1)
    val workoutDurationString: LiveData<String> = _workoutDurationSecs.map {
        val hours = it / 3600
        val minutes = (it % 3600) / 60
        val seconds = it % 60
        String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
    }

    private val _averagePace = MutableLiveData(1) // Средний темп
    val averagePaceString: LiveData<String> = _averagePace.map {
        val minutes = it / 60
        val seconds = it % 60
        String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }

    private val _workoutSteps = MutableLiveData(-1)
    val workoutSteps: LiveData<Int> = _workoutSteps

    private val _workoutSpentKcal = MutableLiveData(-1)
    val workoutSpentKcal: LiveData<Int> = _workoutSpentKcal

    private val _averageHeartRate = MutableLiveData(-1)
    val averageHeartRate: LiveData<Int> = _averageHeartRate

    private val _cadence = MutableLiveData(-1) // Шаги в минуту
    val cadence: LiveData<Int> = _cadence

    private val restingHeartRate = 75

    private var selectedWorkout = 0

    fun nextSelectedWorkout() {
        selectedWorkout++
        //loadData()
    }

    fun previousSelectedWorkout() {
        selectedWorkout--
        //loadData()
    }

    init {
        viewModelScope.launch {
            initializeLastWorkout()
        }
    }

    private suspend fun initializeLastWorkout() {
        val numberOfWorkoutsFlow = database.getWorkoutsCount()

        numberOfWorkoutsFlow.collect { numberOfWorkouts ->
            when (numberOfWorkouts) {
                0 -> {
                    _isNoDataAvailable.value = true
                }
                1 -> {
                    if (justFinishedWorkoutId == -1L) {
                        database.getLastFinishedWorkout().collect {
                            if (it == null) {
                                _isNoDataAvailable.value = true
                            } else {
                                _isNoDataAvailable.value = false
                                val workoutFlow = it
                                loadData(flowOf(workoutFlow))
                            }
                        }
                    } else {
                        val workoutFlow = database.getWorkoutById(justFinishedWorkoutId)
                        loadData(workoutFlow)
                    }
                }
                else -> {
                    val workoutFlow = if (justFinishedWorkoutId == -1L) {
                        database.getLastFinishedWorkout()
                    } else {
                        database.getWorkoutById(justFinishedWorkoutId)
                    }

                    loadData(workoutFlow)
                }
            }
        }
    }

    private suspend fun loadData(workoutFlow: Flow<Workout?>) {
        workoutFlow
            .filterNotNull()
            .filter { it.startTimestamp != it.endTimestamp }
            .collect { workout ->
                val workoutDistanceKM = workout.distanceM.toFloat() / 1000
                val workoutDurationSecs = (workout.endTimestamp - workout.startTimestamp) / 1000
                val stepCount = workout.stepCount
                val workoutId = workout.id
                val heartRateSeries = database.getHeartRateSeriesByWorkoutId(workoutId)

                initDataTable(
                    workoutDistanceKM,
                    workoutDurationSecs,
                    stepCount,
                    workout,
                    heartRateSeries,
                )

                initLineChart(heartRateSeries)
                initPieChart(heartRateSeries)
            }
    }

    private suspend fun initPieChart(heartRateSeries: List<HeartRateSeriesElement>) {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val userBirthYear = HealbeSdk.get().USER.user.await().birthDate.Y
        val userAge = currentYear - userBirthYear

        val maxHeartRate = 220 - userAge

        val workoutIntensitySeries =
            heartRateSeries.map {
                (it.heartRate - restingHeartRate).toDouble() / (maxHeartRate - restingHeartRate)
            }

        val noActivityIntensitySize =
            workoutIntensitySeries.filter { it <= 0.4 }.size.toFloat()
        val warmUpIntensitySize =
            workoutIntensitySeries.filter { it > 0.4 && it <= 0.6 }.size.toFloat()
        val normalIntensitySize =
            workoutIntensitySeries.filter { it > 0.6 && it <= 0.7 }.size.toFloat()
        val aerobicIntensitySize =
            workoutIntensitySeries.filter { it > 0.7 && it <= 0.8 }.size.toFloat()
        val anaerobicIntensitySize =
            workoutIntensitySeries.filter { it > 0.8 && it <= 0.9 }.size.toFloat()
        val maximumIntensitySize =
            workoutIntensitySeries.filter { it > 0.9 }.size.toFloat()

        val pieEntries = listOf(
            PieEntry(noActivityIntensitySize, "В покое"),
            PieEntry(warmUpIntensitySize, "Лёгкая активность"),
            PieEntry(normalIntensitySize, "Сжигание жира"),
            PieEntry(aerobicIntensitySize, "Аэробная"),
            PieEntry(anaerobicIntensitySize, "Анаэробная"),
            PieEntry(maximumIntensitySize, "Максимальная нагрузка"),
        )

        val pieDataSet = PieDataSet(pieEntries, "Зоны пульса").apply {
            colors =
                listOf(
                    Color.LTGRAY,
                    Color.parseColor("#FF9B70"),
                    Color.parseColor("#CB15BE"),
                    Color.parseColor("#00ABFF"),
                    Color.parseColor("#FD0D92"),
                    Color.parseColor("#FD0D0D"),
                )
            valueTextColor = Color.BLACK
        }

        withContext(Dispatchers.Main) {
            _workoutPieChartData.value = PieData(pieDataSet)
        }
    }

    private suspend fun initLineChart(heartRateSeries: List<HeartRateSeriesElement>) {
        val heartRateFirstTimestamp = heartRateSeries.first().timestamp

        val lineChartEntries = heartRateSeries.map {
            val newTimestamp = it.timestamp - heartRateFirstTimestamp
            Entry(newTimestamp.toFloat(), it.heartRate.toFloat())
        }
        val lineDataSet = LineDataSet(lineChartEntries, "")
        val lineData = LineData(lineDataSet)

        withContext(Dispatchers.Main) {
            _workoutLineChartData.value = lineData
        }
    }

    private fun initDataTable(
        workoutDistanceKM: Float,
        workoutDurationSecs: Long,
        stepCount: Int,
        workout: Workout,
        heartRateSeries: List<HeartRateSeriesElement>
    ) {
        _workoutDistanceKM.value = workoutDistanceKM

        _workoutDurationSecs.value = workoutDurationSecs
        _averagePace.value = if (workoutDistanceKM != 0f) {
            (workoutDurationSecs / workoutDistanceKM).roundToInt() / 60
        } else {
            0
        }
        _workoutSteps.value = stepCount

        _workoutSpentKcal.value = workout.spentKcal
        _cadence.value = if (workoutDurationSecs != 0L) {
            (stepCount / (workoutDurationSecs.toDouble() / 60)).roundToInt()
        } else {
            0
        }

        _averageHeartRate.value =
            heartRateSeries.map { it.heartRate }.average().roundToInt()
    }
}
