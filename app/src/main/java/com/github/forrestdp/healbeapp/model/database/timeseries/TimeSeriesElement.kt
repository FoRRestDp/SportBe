package com.github.forrestdp.healbeapp.model.database.timeseries

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "time_series_table")
data class TimeSeriesElement(
    @PrimaryKey @ColumnInfo
    val timestamp: Long = -1L,
    @ColumnInfo
    val bpm: Int = -1,
)
