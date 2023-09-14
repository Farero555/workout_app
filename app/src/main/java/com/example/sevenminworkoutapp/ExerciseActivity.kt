package com.example.sevenminworkoutapp


import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sevenminworkoutapp.databinding.ActivityExerciseBinding
import com.example.sevenminworkoutapp.databinding.DialogCustomBackConfirmationBinding
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding : ActivityExerciseBinding? = null
    private var restTimer : CountDownTimer? = null
    private var restProgress = 0
    private var timerMaxTimeRest : Long = 30000 //30000

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var timerMaxTimeExercise : Long = 1000 //exercise time for testing
    private var pausePressed : Boolean = false

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = 0
    private var constraints: ConstraintSet? = null

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null
    private var remainingTime: Long? = null

    private var exerciseAdapter : ExerciseStatusAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        constraints = ConstraintSet()
        constraints?.clone(binding?.root)

        exerciseList = Constants.defaultExerciseList()

        binding?.toolbarExercise?.setNavigationOnClickListener {

            customDialogForBackButton()

        }
        binding?.flSkipBtn?.setOnClickListener {
            if(currentExercisePosition < exerciseList?.size!!){
                skipExercise()
            }

        }
        binding?.flPauseBtn?.setOnClickListener {
            pauseTimer()
        }
        tts = TextToSpeech(this ,this)

        binding?.tvTimer?.text = (timerMaxTimeRest/1000).toString()

        setupRestView()
        setupExerciseStatusRecyclerView()

    }
    private fun pauseTimer(){
        if(binding?.tvExercise?.visibility == View.VISIBLE){
            val tempExerciseTimer = exerciseTimer
            remainingTime = (exerciseList!![currentExercisePosition].getExerciseTimer()/1000) - exerciseProgress
            if(pausePressed){
                exerciseTimer = tempExerciseTimer
                setExerciseProgressBar()
                pausePressed = false
            }else{
                exerciseTimer?.cancel()
                var min = remainingTime!!/60
                var remSec = remainingTime!!%60
                binding?.tvTimer?.text = String.format("%02d:%02d", min, remSec)
                pausePressed = true
            }

        }
    }

    private fun skipExercise(){
        if(binding?.tvExercise?.visibility == View.VISIBLE){
            exerciseTimer?.cancel()
            exerciseList!![currentExercisePosition].setIsSelected(false)
            exerciseList!![currentExercisePosition].setIsCompleted(true)
            exerciseAdapter!!.notifyDataSetChanged()
            if(currentExercisePosition < exerciseList?.size!!-1){
                currentExercisePosition++
                setupRestView()
            }else{
                val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                startActivity(intent)
                finish()
            }

            setupRestView()
        }else{
            restTimer?.cancel()
            exerciseList!![currentExercisePosition].setIsSelected(true)
            exerciseAdapter!!.notifyDataSetChanged()


            setupExerciseView()
        }
    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()


    }

    override fun onBackPressed() {
        customDialogForBackButton()

    }

    private fun setupExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter

    }



    private fun setupRestView(){

        try{
            val soundURI = Uri.parse(
                "android.resource://com.example.sevenminworkoutapp/"+ R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()

        }catch(e: Exception){
            e.printStackTrace()
        }

        if(currentExercisePosition < exerciseList?.size!!){
            binding?.progressBar?.max = (timerMaxTimeRest/1000).toInt()

            constraints?.clear(binding?.flTimer!!.id, ConstraintSet.TOP)
            constraints?.connect(binding?.flTimer!!.id, ConstraintSet.TOP, binding?.toolbarExercise!!.id, ConstraintSet.BOTTOM)
            constraints?.clear(binding?.rvExerciseStatus!!.id, ConstraintSet.TOP)
            constraints?.applyTo(binding?.root)

            binding?.flPauseBtn?.visibility = View.INVISIBLE

            binding?.tvTitle?.visibility = View.VISIBLE
            binding?.tvExercise?.visibility = View.INVISIBLE
            binding?.ivImage?.visibility = View.INVISIBLE
            binding?.tvUpcomingExercise?.visibility = View.VISIBLE
            binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE
            binding?.tvUpcomingExerciseName?.text = exerciseList!![currentExercisePosition].getName()



            if(restTimer != null){
                restTimer?.cancel()
                restProgress = 0
            }


            setRestProgressBar()
        }

    }
    private fun setupExerciseView(){
//        binding?.progressBar?.max = (timerMaxTimeExercise/1000).toInt()
        binding?.progressBar?.max = (exerciseList!![currentExercisePosition].getExerciseTimer()/1000).toInt()


        constraints?.clear(binding?.flTimer!!.id, ConstraintSet.TOP)
        constraints?.connect(binding?.flTimer!!.id, ConstraintSet.TOP, binding?.tvExercise!!.id, ConstraintSet.BOTTOM)
        constraints?.connect(binding?.rvExerciseStatus!!.id, ConstraintSet.TOP, binding?.flTimer!!.id, ConstraintSet.BOTTOM)
        constraints?.applyTo(binding?.root)

        binding?.flPauseBtn?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExercise?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.tvUpcomingExercise?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE





        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        ttsNextWorkout(exerciseList!![currentExercisePosition].getName())

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExercise?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProgress
        restTimer = object : CountDownTimer(timerMaxTimeRest, 1000){
            override fun onTick(millisUntilFinished: Long){
                restProgress++
                binding?.progressBar?.progress = (timerMaxTimeRest/1000).toInt() - restProgress
                var timeInSec = (timerMaxTimeRest/1000).toInt() - restProgress
                var min = timeInSec/60
                var remSec = timeInSec%60
                binding?.tvTimer?.text = String.format("%02d:%02d", min, remSec)

            }

            override fun onFinish() {
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()


                setupExerciseView()

            }

        }.start()



    }

    private fun setExerciseProgressBar(){

        binding?.progressBar?.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(if(pausePressed)  exerciseList!![currentExercisePosition].getExerciseTimer()-exerciseProgress.toLong() else exerciseList!![currentExercisePosition].getExerciseTimer(), 1000){
            override fun onTick(millisUntilFinished: Long){
                exerciseProgress++
                binding?.progressBar?.progress = (exerciseList!![currentExercisePosition].getExerciseTimer()/1000).toInt() - exerciseProgress
                var timeInSec = (exerciseList!![currentExercisePosition].getExerciseTimer()/1000).toInt() - exerciseProgress
//                binding?.progressBar?.progress = (timerMaxTimeExercise/1000).toInt() - exerciseProgress
//                var timeInSec = (timerMaxTimeExercise/1000).toInt() - exerciseProgress
                var min = timeInSec/60
                var remSec = timeInSec%60
                binding?.tvTimer?.text = String.format("%02d:%02d", min, remSec)
                if(min>0 && remSec==0){
                    ttsNextWorkout("$min minutes remaining")
                }

            }

            override fun onFinish() {
                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseAdapter!!.notifyDataSetChanged()
                if(currentExercisePosition < exerciseList?.size!!-1){
                    currentExercisePosition++
                    setupRestView()
                }else{
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        if(tts != null){
            tts?.stop()
            tts?.shutdown()
        }
        if(player != null){
            player!!.stop()

        }

        binding = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.ENGLISH)

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","Language not supported")
            }
        }else{
            Log.e("TTS", "Initialization Failed")
        }
    }

    private fun ttsNextWorkout(text: String){
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}