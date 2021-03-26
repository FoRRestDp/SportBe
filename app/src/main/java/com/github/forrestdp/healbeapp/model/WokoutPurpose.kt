package com.github.forrestdp.healbeapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class WorkoutPurpose: Parcelable {
    BE_FIT, FAT_BURNING, STAMINA_DEVELOPMENT,
}