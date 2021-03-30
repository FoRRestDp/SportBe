package com.github.forrestdp.healbeapp.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts_table")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "start_timestamp") val startTimestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "end_timestamp") val endTimestamp: Long = startTimestamp,
    @ColumnInfo(name = "distance_m") val distanceM: Int = -1,
    @ColumnInfo(name = "step_count") val stepCount: Int = -1,
    @ColumnInfo(name = "spent_kcal") val spentKcal: Int = -1,
)
