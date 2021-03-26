package com.github.forrestdp.healbeapp.model.database.timeseries

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeSeriesDao {
    @Insert
    fun insertAll(element: List<TimeSeriesElement>)

    @Query(
        """
select * from time_series_table 
where timestamp >= :startTimestamp and timestamp <= :endTimestamp and bpm <> 0 
order by timestamp asc
        """
    )
    fun getWorkout(startTimestamp: Long, endTimestamp: Long): Flow<List<TimeSeriesElement>>

    @Query("select * from time_series_table where timestamp <> 0 and bpm <> 0 order by timestamp asc")
    fun getWholeTimeSeries(): List<TimeSeriesElement>
}