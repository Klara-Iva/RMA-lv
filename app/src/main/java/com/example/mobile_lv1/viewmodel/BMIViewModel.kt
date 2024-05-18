package com.example.mobile_lv1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.example.mobile_lv1.model.BMIModel
class BMIViewModel : ViewModel() {
    val bmiModel = BMIModel(weight = null, height = null, bmiResult = null)
    fun calculateBMI(weight: Float?, height: Float?): Float? {
        return if (weight != null && height != null && height > 0) {
            val bmi = weight / ((height / 100) * (height / 100))
            bmiModel.bmiResult = bmi
            bmi //this is what is returning
        } else {
            bmiModel.bmiResult = null
            null
        }

    }
}