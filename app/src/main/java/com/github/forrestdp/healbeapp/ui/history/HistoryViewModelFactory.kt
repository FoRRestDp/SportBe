package com.github.forrestdp.healbeapp.ui.history

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.forrestdp.healbeapp.model.database.SportBeDatabaseDao
import com.github.forrestdp.healbeapp.model.database.entities.Workout
import java.lang.IllegalArgumentException

class HistoryViewModelFactory(
    private val dataSource: SportBeDatabaseDao,
    private val justFinishedWorkoutId: Long,
    private val application: Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(dataSource, justFinishedWorkoutId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}