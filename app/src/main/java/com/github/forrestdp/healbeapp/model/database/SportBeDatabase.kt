package com.github.forrestdp.healbeapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.forrestdp.healbeapp.model.database.entities.HeartRateSeriesElement
import com.github.forrestdp.healbeapp.model.database.entities.Workout

@Database(entities = [HeartRateSeriesElement::class, Workout::class], version = 5, exportSchema = false)
abstract class SportBeDatabase : RoomDatabase() {

    abstract val sportBeDatabaseDao: SportBeDatabaseDao

    companion object {
        @Volatile private var INSTANCE: SportBeDatabase? = null

        fun getInstance(context: Context): SportBeDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SportBeDatabase::class.java,
                        "sportbe_database"
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