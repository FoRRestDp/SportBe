package com.github.forrestdp.healbeapp.ui.timer

import android.app.Application
import androidx.lifecycle.*
import com.github.forrestdp.healbeapp.model.database.SportBeDatabaseDao
import com.github.forrestdp.healbeapp.model.database.entities.HeartRateSeriesElement
import com.github.forrestdp.healbeapp.model.database.entities.Workout
import com.healbe.healbesdk.business_api.HealbeSdk
import com.healbe.healbesdk.business_api.user.data.DistanceUnits
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.rx2.asFlow
import java.util.*
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class TimerViewModel(
    private val sportBeDatabase: SportBeDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val currentWorkoutHeartRateSeries = mutableListOf<HeartRateSeriesElement>()
    private val _pulse = MutableLiveData(0)
    val pulse: LiveData<Int> = _pulse

    private val _isWorkoutPaused = MutableLiveData(false)
    val isWorkoutPaused: LiveData<Boolean> = _isWorkoutPaused

    private val _currentWorkoutId = MutableLiveData<Long?>(null)

    val isWorkoutStopped: LiveData<Boolean> = _currentWorkoutId.map { it == null }

    private val _currentTimeSecs = MutableLiveData(0L)
    val time: LiveData<String> = _currentTimeSecs.map {
        val hours = it / 3600
        val minutes = (it % 3600) / 60
        val seconds = it % 60
        String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
    }

    private val _navigateToHistoryFragment = MutableLiveData<Long?>()
    val navigateToHistoryFragment: LiveData<Long?> = _navigateToHistoryFragment

    private lateinit var timerJob: Job
    private lateinit var heartObservationJob: Job

    private var stepCountOnStart = -1
    private var spentKcalOnStart = -1
    private var distanceMOnStart = -1

    fun startOrResumeWorkout() {
        _isWorkoutPaused.value = false
        timerJob = viewModelScope.launch {
            startTimer()
        }

        heartObservationJob = viewModelScope.launch {
            startHeartRateObservation()
        }

        viewModelScope.launch(Dispatchers.IO) {
            val energySummary = HealbeSdk.get().HEALTH_DATA.getEnergySummary(0).awaitFirst().get()
            stepCountOnStart = energySummary?.steps ?: 0
            println("DAVE: steps at start ${energySummary?.steps ?: 0}")
            spentKcalOnStart = energySummary?.activityKcal ?: 0
            distanceMOnStart = energySummary?.getDistance(DistanceUnits.M)?.roundToInt() ?: 0
        }
    }

    fun pauseWorkout() {
        _isWorkoutPaused.value = true
        timerJob.cancel()
        heartObservationJob.cancel()
        _pulse.value = 0
    }

    fun stopWorkoutAndNavigateToHistory() {
        _isWorkoutPaused.value = false

        timerJob.cancel()
        _currentTimeSecs.value = 0

        heartObservationJob.cancel()
        val lastWorkoutId = requireNotNull(_currentWorkoutId.value)
        _pulse.value = 0
        _currentWorkoutId.value = null

        _navigateToHistoryFragment.value = lastWorkoutId

        viewModelScope.launch(Dispatchers.IO) {
            if (currentWorkoutHeartRateSeries.isEmpty()) {
                val lastWorkout =
                    requireNotNull(sportBeDatabase.getWorkoutById(lastWorkoutId).first())
                sportBeDatabase.deleteWorkout(lastWorkout)
                return@launch
            }

            sportBeDatabase.insertHeartRateSeries(currentWorkoutHeartRateSeries)

            currentWorkoutHeartRateSeries.clear()

            val endTimestamp = System.currentTimeMillis()

            val energySummary = HealbeSdk.get().HEALTH_DATA.getEnergySummary(0).awaitFirst().get()

            val stepCountAtEnd = energySummary?.steps ?: 0
            println("DAVE: steps at end: $stepCountAtEnd")
            val stepCount = stepCountAtEnd - stepCountOnStart
            stepCountOnStart = -1

            val distanceMAtEnd = energySummary?.getDistance(DistanceUnits.M)?.roundToInt() ?: 0
            val distanceM = distanceMAtEnd - distanceMOnStart
            distanceMOnStart = -1

            val spentKCalAtEnd = energySummary?.activityKcal ?: 0
            val spentKCal = spentKCalAtEnd - spentKcalOnStart
            spentKcalOnStart = -1

            val lastWorkout = requireNotNull(sportBeDatabase.getWorkoutById(lastWorkoutId).first())
            val newWorkout = lastWorkout.copy(
                endTimestamp = endTimestamp,
                distanceM = distanceM,
                stepCount = stepCount,
                spentKcal = spentKCal,
            )
            println("KATE: $newWorkout")
            sportBeDatabase.updateWorkout(newWorkout)
        }
    }

    fun stopWorkoutComplete() {
        _navigateToHistoryFragment.value = null
    }

    private suspend fun startTimer() = withContext(Dispatchers.Main) {
        while (isActive) {
            delay(1000)
            _currentTimeSecs.value = _currentTimeSecs.value?.plus(1)
        }
    }

    private suspend fun startHeartRateObservation() {
        if (_currentWorkoutId.value == null) {
            _currentWorkoutId.value = sportBeDatabase.insertWorkout(Workout())

        }

        withContext(Dispatchers.Main) {
            HealbeSdk.get().TASKS.observeHeartRate().asFlow()
                .cancellable()
                .flowOn(Dispatchers.IO)
                .collect {
                    if (!it.isEmpty && it.heartRate != 0) {
                        val currWoId = requireNotNull(_currentWorkoutId.value)
                        currentWorkoutHeartRateSeries.add(
                            HeartRateSeriesElement(it.timestamp, currWoId, it.heartRate)
                        )
                        _pulse.value = it.heartRate
                    }
                }
        }
    }
}
