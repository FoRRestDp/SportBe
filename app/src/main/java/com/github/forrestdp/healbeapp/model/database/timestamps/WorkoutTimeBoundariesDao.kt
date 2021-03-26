package com.github.forrestdp.healbeapp.model.database.timestamps

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkoutTimeBoundariesDao {
    @Query("select * from workouts_table order by start_timestamp asc")
    fun getAllWorkouts(): List<WorkoutTimeBoundaries>

    @Insert
    fun insertWorkout(workoutTimeBoundaries: WorkoutTimeBoundaries)
}