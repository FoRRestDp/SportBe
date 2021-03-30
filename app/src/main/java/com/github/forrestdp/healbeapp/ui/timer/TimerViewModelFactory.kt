package com.github.forrestdp.healbeapp.ui.timer

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.forrestdp.healbeapp.model.database.SportBeDatabaseDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.IllegalArgumentException
import kotlin.time.ExperimentalTime

class TimerViewModelFactory(
    private val sportBeDatabaseDataSource: SportBeDatabaseDao,
    private val application: Application,
) : ViewModelProvider.Factory {
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel(sportBeDatabaseDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}