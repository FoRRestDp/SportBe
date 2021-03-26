package com.github.forrestdp.healbeapp.ui.history

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.forrestdp.healbeapp.model.database.timeseries.TimeSeriesDao
import com.github.forrestdp.healbeapp.model.database.timestamps.WorkoutTimeBoundaries
import com.github.forrestdp.healbeapp.model.database.timestamps.WorkoutTimeBoundariesDao
import java.lang.IllegalArgumentException

class HistoryViewModelFactory(
    private val dataSource: TimeSeriesDao,
    private val application: Application,
    private val workoutTimeBoundaries: WorkoutTimeBoundaries,
    private val dataSourceWorkout: WorkoutTimeBoundariesDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(dataSource, application, workoutTimeBoundaries, dataSourceWorkout) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}