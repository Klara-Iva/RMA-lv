package com.example.mobile_lv1.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobile_lv1.viewmodel.StepCounterViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun StepCounterScreen(navController: NavController, stepCounterViewModel: StepCounterViewModel) {
    val context = LocalContext.current
    val stepCount by stepCounterViewModel.stepCounterModel.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Step Count:",
                fontSize = 46.sp,
                lineHeight = 56.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "${stepCount.steps}",
                fontSize = 56.sp,
                lineHeight = 56.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        stepCounterViewModel.saveStepsToDatabase(
                            userId = it.uid,
                            onSuccess = {
                                Toast.makeText(context, "Steps saved successfully", Toast.LENGTH_SHORT).show()
                            },
                            onFailure = { e ->
                                Toast.makeText(context, "Failed to save steps: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Save steps")
            }
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Back to BMI Calculator")
            }
        }
    }
}
