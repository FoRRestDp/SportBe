package com.github.forrestdp.healbeapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.forrestdp.healbeapp.model.database.timeseries.TimeSeriesDao
import com.github.forrestdp.healbeapp.model.database.timeseries.TimeSeriesElement
import com.github.forrestdp.healbeapp.model.database.timestamps.WorkoutTimeBoundaries
import com.github.forrestdp.healbeapp.model.database.timestamps.WorkoutTimeBoundariesDao

@Database(entities = [TimeSeriesElement::class, WorkoutTimeBoundaries::class], version = 3, exportSchema = false)
abstract class TimeSeriesDatabase : RoomDatabase() {

    abstract val timeSeriesDao: TimeSeriesDao
    abstract val workoutBoundariesDao: WorkoutTimeBoundariesDao

    companion object {
        @Volatile private var INSTANCE: TimeSeriesDatabase? = null

        fun getInstance(context: Context): TimeSeriesDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TimeSeriesDatabase::class.java,
                        "time_series_table"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}