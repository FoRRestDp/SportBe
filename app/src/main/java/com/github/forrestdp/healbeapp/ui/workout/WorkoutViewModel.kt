package com.github.forrestdp.healbeapp.ui.workout

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.forrestdp.healbeapp.model.WorkoutMode
import com.github.forrestdp.healbeapp.model.WorkoutPurpose
import com.github.forrestdp.healbeapp.model.WorkoutSettings
import com.github.forrestdp.healbeapp.util.FragmentToolbarable

class WorkoutViewModel : ViewModel(), FragmentToolbarable {

    override val title = "Тренировка"
    override lateinit var titleImage: Drawable

    private val _startWorkoutInProgressActivity = MutableLiveData<WorkoutSettings?>()
    val startWorkoutInProgressActivity: LiveData<WorkoutSettings?> = _startWorkoutInProgressActivity

    private val workoutSettings = WorkoutSettings(WorkoutMode.RUNNING, WorkoutPurpose.BE_FIT)

    fun setWorkoutMode(mode: WorkoutMode) {
        workoutSettings.mode = mode
    }

    fun setWorkoutPurpose(purpose: WorkoutPurpose) {
        workoutSettings.purpose = purpose
    }

    fun startWorkout() {
        _startWorkoutInProgressActivity.value = workoutSettings
    }

    fun startWorkoutComplete() {
        _startWorkoutInProgressActivity.value = null
    }
}