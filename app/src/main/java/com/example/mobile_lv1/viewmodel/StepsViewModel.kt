package com.example.mobile_lv1.viewmodel

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import com.example.mobile_lv1.model.StepCounterModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.sqrt

class StepCounterViewModel(context: Context) : ViewModel() {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val _stepCounterModel = MutableStateFlow(StepCounterModel())
    val stepCounterModel: StateFlow<StepCounterModel> get() = _stepCounterModel
    var isSensorInitialised :Boolean=false
    private val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        override fun onSensorChanged(event: SensorEvent) {

            if (event.sensor == sensor) {
                val x_acceleration = event.values[0]
                val y_acceleration = event.values[1]
                val z_acceleration = event.values[2]
                val magnitude = sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration)
                val magnitudeDelta = (magnitude - _stepCounterModel.value.magnitudePrevious).toDouble()
                _stepCounterModel.value = _stepCounterModel.value.copy(
                    magnitudePrevious = magnitude,
                    magnitudeDelta = magnitudeDelta
                )
                if (magnitudeDelta > 6 && isSensorInitialised) {
                    _stepCounterModel.value = _stepCounterModel.value.copy(steps = _stepCounterModel.value.steps + 1)
                }
            }

            isSensorInitialised=true
        }
    }

    init {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun saveStepsToDatabase(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userSteps = hashMapOf("steps" to _stepCounterModel.value.steps)
        db.collection("BMI").document(userId)
            .set(userSteps)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
