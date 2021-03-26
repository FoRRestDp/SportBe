package com.github.forrestdp.healbeapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class WorkoutMode: Parcelable {
    RUNNING, CYCLING, FITNESS, YOGA,
}