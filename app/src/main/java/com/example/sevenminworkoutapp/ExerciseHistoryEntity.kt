package com.example.sevenminworkoutapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise-history")
data class ExerciseHistoryEntity(
    @PrimaryKey
    val date:String

)
