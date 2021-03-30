package com.github.forrestdp.healbeapp.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "heart_rate_series")
data class HeartRateSeriesElement(
    @PrimaryKey val timestamp: Long,
    @ColumnInfo(name = "workout_id") val workoutId: Long,
    @ColumnInfo(name = "heart_rate") val heartRate: Int,
)