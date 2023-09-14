package com.example.sevenminworkoutapp

import android.app.Application

class WorkOutApp: Application() {

    val db by lazy{
        ExerciseHistoryDB.getInstance(this)
    }
}