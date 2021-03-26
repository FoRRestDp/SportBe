package com.github.forrestdp.healbeapp.ui.timer

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.forrestdp.healbeapp.model.database.timeseries.TimeSeriesDao
import com.github.forrestdp.healbeapp.model.database.timestamps.WorkoutTimeBoundariesDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.IllegalArgumentException

class TimerViewModelFactory(
    private val timeSeriesDataSource: TimeSeriesDao,
    private val workoutBoundariesDataSource: WorkoutTimeBoundariesDao,
    private val application: Application,
) : ViewModelProvider.Factory {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel(timeSeriesDataSource, workoutBoundariesDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}