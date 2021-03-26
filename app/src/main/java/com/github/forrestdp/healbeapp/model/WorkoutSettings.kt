package com.github.forrestdp.healbeapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutSettings(var mode: WorkoutMode, var purpose: WorkoutPurpose): Parcelable
