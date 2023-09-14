package com.example.sevenminworkoutapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExerciseHistoryEntity::class],version = 1)
abstract class ExerciseHistoryDB:RoomDatabase() {

    abstract fun exerciseHistoryDao(): ExerciseHistoryDao

    companion object{

        @Volatile
        private var INSTANCE: ExerciseHistoryDB? = null

        fun getInstance(context: Context):ExerciseHistoryDB{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ExerciseHistoryDB::class.java,
                        "exercise_history_db"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}