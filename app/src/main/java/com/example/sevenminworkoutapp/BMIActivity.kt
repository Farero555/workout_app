package com.example.sevenminworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.sevenminworkoutapp.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
        private const val US_UNITS_VIEW = "US_UNITS_VIEW"

    }

    private var currentVisibleView: String = METRIC_UNITS_VIEW

    private var binding : ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnCalculate?.setOnClickListener {
            if(validateMetricUnits()){
                calculateBMI()
            }else{
                Toast.makeText(this,"Please enter values",Toast.LENGTH_SHORT).show()
            }

        }
        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->

            if(checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUSUnitsView()
            }

        }

    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding?.tilWeight?.hint = "Weight lb"
        binding?.tilHeight?.visibility = View.INVISIBLE
        binding?.tilHeightUSFeet?.visibility = View.VISIBLE
        binding?.tilHeightUSInch?.visibility = View.VISIBLE

        binding?.etHeightUSFeet?.text!!.clear()
        binding?.etHeightUSInch?.text!!.clear()
        binding?.etWeight?.text!!.clear()

        binding?.tvBMI?.visibility = View.INVISIBLE
        binding?.tvBMINum?.visibility = View.INVISIBLE
        binding?.tvBMILevel?.visibility = View.INVISIBLE
        binding?.tvBMIText?.visibility = View.INVISIBLE

    }

    private fun validateMetricUnits(): Boolean{
        var isValid = true

        if(currentVisibleView == METRIC_UNITS_VIEW){
            if(binding?.etWeight?.text.toString().isEmpty()){
                isValid = false
            }else if(binding?.etHeight?.text.toString().isEmpty()){
                isValid = false
            }
        }else{
            if(binding?.etWeight?.text.toString().isEmpty()){
                isValid = false
            }else if(binding?.etHeightUSFeet?.text.toString().isEmpty()){
                isValid = false
            }else if(binding?.etHeightUSInch?.text.toString().isEmpty()){
                isValid = false
            }
        }

        return isValid
    }



    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilWeight?.hint = "Weight kg"
        binding?.tilHeight?.visibility = View.VISIBLE
        binding?.tilHeightUSFeet?.visibility = View.INVISIBLE
        binding?.tilHeightUSInch?.visibility = View.INVISIBLE

        binding?.etHeight?.text!!.clear()
        binding?.etWeight?.text!!.clear()

        binding?.tvBMI?.visibility = View.INVISIBLE
        binding?.tvBMINum?.visibility = View.INVISIBLE
        binding?.tvBMILevel?.visibility = View.INVISIBLE
        binding?.tvBMIText?.visibility = View.INVISIBLE


    }

    private fun calculateBMI(){

        var bmiNum: Float? = null
        if(currentVisibleView == METRIC_UNITS_VIEW){
            val weight: Float = binding?.etWeight?.text.toString().toFloat()
            val height: Float = binding?.etHeight?.text.toString().toFloat()/100
            bmiNum = weight/(height*height)
        }else{
            val heightFeet: Float = binding?.etHeightUSFeet?.text.toString().toFloat()
            val heightInch: Float = binding?.etHeightUSInch?.text.toString().toFloat()
            val weight: Float = binding?.etWeight?.text.toString().toFloat()
            val height: Float = heightFeet*12+heightInch

            bmiNum = weight/(height*height)*703
        }

        binding?.tvBMINum?.text =
            BigDecimal(bmiNum.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()
        if(bmiNum >= 30){
            binding?.tvBMILevel?.text = "Obesity"
            binding?.tvBMIText?.text = "You are in a very dangerous condition! Act now!"
        }else if (bmiNum >= 25){
            binding?.tvBMILevel?.text = "Overweight"
            binding?.tvBMIText?.text = "Oops! You really need to take better care of yourself!"
        }else if (bmiNum >= 18.5){
            binding?.tvBMILevel?.text = "Normal"
            binding?.tvBMIText?.text = "Congratulations! You are in a good shape!"
        }else{
            binding?.tvBMILevel?.text = "Underweight"
            binding?.tvBMIText?.text = "Oops! You really need to take better care of yourself! Eat more!"
        }

        binding?.tvBMI?.visibility = View.VISIBLE
        binding?.tvBMINum?.visibility = View.VISIBLE
        binding?.tvBMILevel?.visibility = View.VISIBLE
        binding?.tvBMIText?.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

