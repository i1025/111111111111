package com.example.a111111111111

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*


import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp


import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.a111111111111.ui.theme._111111111111Theme
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import androidx.compose.runtime.rememberUpdatedState as rememberUpdatedState
import kotlinx.coroutines.launch



class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            MyApp()

        }
    }
}

@Composable
fun MyApp() {
    MaterialTheme {
        InputForm()
    }
}


@Composable
fun InputForm() {
    val mealStatus = remember { mutableStateOf("") }
    val moodIndex = remember { mutableStateOf("") }
    val medicationUsage = remember { mutableStateOf("") }
    val bloodPressure = remember { mutableStateOf("") }
    val bodyTemperature = remember { mutableStateOf("") }
    val notes = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val msg = remember { mutableStateOf("") }


    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = mealStatus.value,
            onValueChange = { mealStatus.value = it },
            label = { Text("進食狀況") }
        )
        TextField(
            value = moodIndex.value,
            onValueChange = { moodIndex.value = it },
            label = { Text("心情指數") }
        )
        TextField(
            value = medicationUsage.value,
            onValueChange = { medicationUsage.value = it },
            label = { Text("藥物使用") }
        )
        TextField(
            value = bloodPressure.value,
            onValueChange = { bloodPressure.value = it },
            label = { Text("血壓") }
        )
        TextField(
            value = bodyTemperature.value,
            onValueChange = { bodyTemperature.value = it },
            label = { Text("體溫") }
        )
        TextField(
            value = notes.value,
            onValueChange = { notes.value = it },
            label = { Text("備註") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                val db = Firebase.firestore
                val data = hashMapOf(
                    "mealStatus" to mealStatus,
                    "moodIndex" to moodIndex,
                    "medicationUsage" to medicationUsage,
                    "bloodPressure" to bloodPressure,
                    "bodyTemperature" to bodyTemperature,
                    "notes" to notes
                )
                coroutineScope.launch {
                    saveDataToFirestore(
                        mealStatus.value,
                        moodIndex.value,
                        medicationUsage.value,
                        bloodPressure.value,
                        bodyTemperature.value,
                        notes.value,
                        msg,
                        snackbarHostState
                    )

                }

            }) {
                Text("新增資料")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = msg.value)

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.BottomCenter)
            )

            /*Button(onClick = {
                coroutineScope.launch {
                }
            }) {
                Text("刪除資料")
            }*/
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomCenter)
    )
}



fun saveDataToFirestore(
    mealStatus: String,
    moodIndex: String,
    medicationUsage: String,
    bloodPressure: String,
    bodyTemperature: String,
    notes: String,
    msg: MutableState<String>,
    snackbarHostState: SnackbarHostState
) {
    val db = Firebase.firestore
    val data = hashMapOf(
        "mealStatus" to mealStatus,
        "moodIndex" to moodIndex,
        "medicationUsage" to medicationUsage,
        "bloodPressure" to bloodPressure,
        "bodyTemperature" to bodyTemperature,
        "notes" to notes
    )

    db.collection("員工")
        .add(data)
        .addOnSuccessListener { msg.value = "新增資料成功"
            showSnackbar(snackbarHostState, "新增資料成功")
        }
        .addOnFailureListener { e ->
            msg.value = "新增資料失敗：$e"
            showSnackbar(snackbarHostState, "新增資料失敗")
        }
}
fun showSnackbar(snackbarHostState: SnackbarHostState, message: String) {
    CoroutineScope(Dispatchers.Main).launch {
        snackbarHostState.showSnackbar(message)
    }
}


