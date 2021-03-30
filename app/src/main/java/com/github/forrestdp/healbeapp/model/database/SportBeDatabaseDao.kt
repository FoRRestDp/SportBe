package com.github.forrestdp.healbeapp.model.database

import androidx.room.*
import com.github.forrestdp.healbeapp.model.database.entities.HeartRateSeriesElement
import com.github.forrestdp.healbeapp.model.database.entities.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface SportBeDatabaseDao {
    @Insert
    suspend fun insertHeartRateSeries(elements: List<HeartRateSeriesElement>)

    @Query(
        """
        SELECT * FROM heart_rate_series
        WHERE workout_id = :workoutId
        ORDER BY timestamp ASC
    """
    )
    suspend fun getHeartRateSeriesByWorkoutId(workoutId: Long): List<HeartRateSeriesElement>

//    @Query("select * from heart_rate_series where workout_id <> 0 and heart_rate <> 0 order by timestamp asc")
//    fun getWholeTimeSeries(): List<HeartRateSeriesElement>

    @Insert
    suspend fun insertWorkout(workout: Workout): Long

    @Query(
        """
            SELECT * FROM workouts_table
            WHERE id = :workoutId 
            LIMIT 1
        """
    )
    fun getWorkoutById(workoutId: Long): Flow<Workout?>

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Query(
        """
        SELECT * FROM workouts_table
        WHERE start_timestamp <> end_timestamp
        ORDER BY start_timestamp DESC
        LIMIT 1
    """
    )
    fun getLastFinishedWorkout(): Flow<Workout?>
}