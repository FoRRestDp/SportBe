package com.github.forrestdp.healbeapp.ui.progress

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.forrestdp.healbeapp.model.database.timestamps.WorkoutTimeBoundariesDao
import com.github.forrestdp.healbeapp.ui.timer.TimerViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.IllegalArgumentException

class ProgressViewModelFactory(
    private val workoutBoundariesDataSource: WorkoutTimeBoundariesDao,
    private val application: Application,
) : ViewModelProvider.Factory {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProgressViewModel::class.java)) {
            return ProgressViewModel(workoutBoundariesDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}