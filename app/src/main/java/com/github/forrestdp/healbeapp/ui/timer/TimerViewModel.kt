package com.github.forrestdp.healbeapp.ui.timer

import android.app.Application
import androidx.lifecycle.*
import com.github.forrestdp.healbeapp.model.database.timeseries.TimeSeriesDao
import com.github.forrestdp.healbeapp.model.database.timeseries.TimeSeriesElement
import com.github.forrestdp.healbeapp.model.database.timestamps.WorkoutTimeBoundaries
import com.github.forrestdp.healbeapp.model.database.timestamps.WorkoutTimeBoundariesDao
import com.github.forrestdp.healbeapp.util.create
import com.healbe.healbesdk.business_api.HealbeSdk
import com.healbe.healbesdk.business_api.user.data.HealbeSessionState
import com.healbe.healbesdk.business_api.user_storage.entity.HealbeDevice
import com.healbe.healbesdk.device_api.ClientState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.rx2.asFlow
import kotlinx.coroutines.rx2.await
import java.util.*

@ExperimentalCoroutinesApi
class TimerViewModel(
    val timeSeriesTable: TimeSeriesDao,
    val workoutBoundariesTable: WorkoutTimeBoundariesDao,
    application: Application
) : AndroidViewModel(application) {

    private val email: String = "egorponomarev93@gmail.com"
    private val password: String = "dYnfoc-quxmiq-4fewka"
    private val device: HealbeDevice = HealbeDevice.create(
        name = "Healbe 78:9B:AF",
        mac = "88:6B:0F:78:9B:AF",
        pin = "091297",
    )

    private val pulseValues = mutableListOf<TimeSeriesElement>()

    private val _pulse = MutableLiveData(0)
    val pulse: LiveData<Int> = _pulse

    private val _time = MutableLiveData("00:00:00")
    val time: LiveData<String> = _time

    private val _navigateToHistoryFragment = MutableLiveData<WorkoutTimeBoundaries?>(null)
    val navigateToHistoryFragment: LiveData<WorkoutTimeBoundaries?> = _navigateToHistoryFragment

    private var elapsedSeconds = 0
    private var isTimerWorking = true

    private var startTimestamp: Long = 0
    private var endTimestamp: Long = 0

    init {
        startTimer()

        viewModelScope.launch {
            HealbeSdk.init(application.applicationContext).await()
            with(HealbeSdk.get()) {
//                withContext(Dispatchers.IO) {
//                    USER.prepareSession().await()
//                    println("EGOR: prepared session")
//                    val sessionState = USER.login(email, password).await()
//                    println("EGOR: logged in")
//                    if (!HealbeSessionState.isUserValid(sessionState)) {
////                    throw RuntimeException("user invalid")
//                        println("User is invalid")
//                    }
//                    GOBE.set(device).await()
//                    println("EGOR: set device")
//                    GOBE.connect().await()
//                    println("EGOR: connected")
//                    GOBE.observeConnectionState().asFlow()
////                        .onEach { _status.value = it.toString() }
//                        .takeWhile { it != ClientState.READY }
//                        .collect()
//                }
                TASKS.observeHeartRate().asFlow()
                    .flowOn(Dispatchers.IO)
                    .onEach {
                        println("EGOR: ${it.heartRate}")
                    }
                    .collect {
                        if (!it.isEmpty && it.heartRate != 0) {
                            if (startTimestamp == 0L) {
                                startTimestamp = it.timestamp
                            }
                            pulseValues.add(TimeSeriesElement(it.timestamp, it.heartRate))
                            _pulse.value = it.heartRate
                        }
                    }

            }
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (isTimerWorking) {
                delay(1000)
                elapsedSeconds++
                val hours = elapsedSeconds / 3600
                val minutes = (elapsedSeconds % 3600) / 60
                val seconds = elapsedSeconds % 60
                val time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
                _time.postValue(time)
            }
        }
    }

    fun stopWorkout() {
        // HealbeSdk.get().TASKS.stopTasks()
        viewModelScope.launch(Dispatchers.IO) {
            timeSeriesTable.insertAll(pulseValues)
        }
        println("EGOR: $pulseValues")
        endTimestamp = pulseValues.lastOrNull()?.timestamp ?: startTimestamp
        val resultWorkout = WorkoutTimeBoundaries(
            startTimestamp = startTimestamp,
            endTimestamp = endTimestamp,
        )

        viewModelScope.launch(Dispatchers.IO) {
            if (startTimestamp != endTimestamp) {
                workoutBoundariesTable.insertWorkout(resultWorkout)
            }
        }
        _navigateToHistoryFragment.value = resultWorkout
    }

    fun stopWorkoutComplete() {
        _navigateToHistoryFragment.value = null
    }
}
