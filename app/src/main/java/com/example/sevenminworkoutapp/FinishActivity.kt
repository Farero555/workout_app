package com.example.sevenminworkoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.sevenminworkoutapp.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FinishActivity : AppCompatActivity() {

    private var binding : ActivityFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnFinish?.setOnClickListener {
            finish()

        }

        val historyDao = (application as WorkOutApp).db.exerciseHistoryDao()
        addDateToDB(historyDao)

    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

    private fun addDateToDB(exerciseHistoryDao: ExerciseHistoryDao){

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        val currentDate = Date()

        lifecycleScope.launch{
            exerciseHistoryDao.insert(ExerciseHistoryEntity(sdf.format(currentDate)))
        }
    }
}