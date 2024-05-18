package com.example.mobile_lv1.model

data class StepCounterModel(
    var steps: Int = 0,
    var magnitudePrevious: Float = 0f,
    var magnitudeDelta: Double = 0.0
)
