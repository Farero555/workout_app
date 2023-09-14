package com.example.sevenminworkoutapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseHistoryDao {
    @Insert
    suspend fun insert(exerciseHistoryEntity: ExerciseHistoryEntity)

    @Query("SELECT * FROM `exercise-history`")
    fun fetchAllDates(): Flow<List<ExerciseHistoryEntity>>
}