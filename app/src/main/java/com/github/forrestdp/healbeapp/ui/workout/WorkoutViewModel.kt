package com.github.forrestdp.healbeapp.ui.workout

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.github.forrestdp.healbeapp.model.WorkoutMode
import com.github.forrestdp.healbeapp.model.WorkoutPurpose
import com.github.forrestdp.healbeapp.model.WorkoutSettings
import com.github.forrestdp.healbeapp.util.FragmentToolbarable

class WorkoutViewModel : ViewModel(), FragmentToolbarable {

    override val title = "Тренировка"
    override lateinit var titleImage: Drawable

    private val _startWorkoutInProgressActivity = MutableLiveData<WorkoutSettings?>()
    val startWorkoutInProgressActivity: LiveData<WorkoutSettings?> = _startWorkoutInProgressActivity

    private val _currentWorkoutMode = MutableLiveData(WorkoutMode.RUNNING)
    private val _currentWorkoutPurpose = MutableLiveData(WorkoutPurpose.BE_FIT)

    val isRunningSelected: LiveData<Boolean> = _currentWorkoutMode.map { it == WorkoutMode.RUNNING }
    val isCyclingSelected: LiveData<Boolean> = _currentWorkoutMode.map { it == WorkoutMode.CYCLING }
    val isFitnessSelected: LiveData<Boolean> = _currentWorkoutMode.map { it == WorkoutMode.FITNESS }
    val isYogaSelected: LiveData<Boolean> = _currentWorkoutMode.map { it == WorkoutMode.YOGA }

    val isBeFitSelected: LiveData<Boolean> =
        _currentWorkoutPurpose.map { it == WorkoutPurpose.BE_FIT }
    val isFatBurningSelected: LiveData<Boolean> =
        _currentWorkoutPurpose.map { it == WorkoutPurpose.FAT_BURNING }
    val isStaminaSelected: LiveData<Boolean> =
        _currentWorkoutPurpose.map { it == WorkoutPurpose.STAMINA_DEVELOPMENT }

    fun runningChecked() = setWorkoutMode(WorkoutMode.RUNNING)
    fun cyclingChecked() = setWorkoutMode(WorkoutMode.CYCLING)
    fun fitnessChecked() = setWorkoutMode(WorkoutMode.FITNESS)
    fun yogaChecked() = setWorkoutMode(WorkoutMode.YOGA)

    fun beFitChecked() = setWorkoutPurpose(WorkoutPurpose.BE_FIT)
    fun fatBurningChecked() = setWorkoutPurpose(WorkoutPurpose.FAT_BURNING)
    fun staminaChecked() = setWorkoutPurpose(WorkoutPurpose.STAMINA_DEVELOPMENT)

    private fun setWorkoutMode(mode: WorkoutMode) {
        _currentWorkoutMode.value = mode
    }

    private fun setWorkoutPurpose(purpose: WorkoutPurpose) {
        _currentWorkoutPurpose.value = purpose
    }

    fun startWorkout() {
        val mode = requireNotNull(_currentWorkoutMode.value)
        val purpose = requireNotNull(_currentWorkoutPurpose.value)
        _startWorkoutInProgressActivity.value = WorkoutSettings(mode, purpose)
    }

    fun startWorkoutComplete() {
        _startWorkoutInProgressActivity.value = null
    }
}