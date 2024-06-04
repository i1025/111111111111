package com.example.a111111111111

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a111111111111.ui.theme._111111111111Theme
//import com.example.a111111111111111.MainActivity3
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // 初始化 Firebase
        setContent {
                Date()
            }
        }
    }

@Composable
fun Date() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "data") {
        composable("data") { Data(navController) }
        composable("detail") { DetailScreen(navController) }
    }
}

@Composable
fun Data(navController: NavHostController) {
    var userName by remember { mutableStateOf("") }
    var userAge by remember { mutableStateOf("") }
    var userSex by remember { mutableStateOf("") }
    var userBirth by remember { mutableStateOf("") }
    var userSmpytom by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }
    var userDate by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("訊息") }
    val db = Firebase.firestore

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = userName,
            onValueChange = { newText -> userName = newText },
            label = { Text("姓名") },
            placeholder = { Text("請輸入您的姓名") }
        )
        TextField(
            value = userAge,
            onValueChange = { newText -> userAge = newText },
            label = { Text("年齡") }
        )
        TextField(
            value = userSex,
            onValueChange = { newText -> userSex = newText },
            label = { Text("性別") }
        )
        TextField(
            value = userBirth,
            onValueChange = { newText -> userBirth = newText },
            label = { Text("生日") }
        )
        TextField(
            value = userSmpytom,
            onValueChange = { newText -> userSmpytom = newText },
            label = { Text("症狀") }
        )
        TextField(
            value = userId,
            onValueChange = { newText -> userId = newText },
            label = { Text("身分證字號") }
        )
        TextField(
            value = userDate,
            onValueChange = { newText -> userDate = newText },
            label = { Text("今天日期") }
        )

        Text("姓名: $userName\n年齡: $userAge 歲" +
                "\n性別: $userSex\n生日: $userBirth\n症狀: $userSmpytom" +
                "\n身分證字號: $userId\n今日日期: $userDate")

        Row {
            Button(onClick = {
                data class Person(
                    var userName: String,
                    var userAge: String,
                    var userSex: String,
                    var userBirth: String,
                    var userSmpytom: String,
                    var userId: String,
                )

                val user = Person(userName, userAge, userSex, userBirth, userSmpytom, userId)
                db.collection("員工")
                    .document(userName)
                    .set(user)
                    .addOnSuccessListener {
                        msg = "新增/異動資料成功"
                    }
                    .addOnFailureListener { e ->
                        msg = "新增/異動資料失敗：" + e.toString()
                    }

            }) {
                Text("新增/修改資料")
            }
            Button(onClick = {
                db.collection("員工")
                    .whereEqualTo("userName", userName)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            msg = ""
                            for (document in task.result!!) {
                                msg += "姓名：" + document.id + "\n年齡：" + document.data["userAge"] +
                                        "\n性別：" + document.data["userSex"] + "\n身分證字號：" + document.data["userId"] + "\n症狀：" + document.data["userSmpytom"] + "\n"
                            }
                            if (msg.isEmpty()) {
                                msg = "查無資料"
                            }
                        }
                    }
            }) {
                Text("查詢資料")
            }
            Button(onClick = {
                db.collection("員工")
                    .document(userName)
                    .delete()
                    .addOnSuccessListener {
                        msg = "刪除資料成功"
                    }
                    .addOnFailureListener { e ->
                        msg = "刪除資料失敗：" + e.toString()
                    }
            }) {
                Text("刪除資料")
            }
        }
        Text(text = msg)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.navigate("detail")
        }) {
            Text("詳細資料")
        }
    }
}

@Composable
fun DetailScreen(navController: NavHostController) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val intent = Intent(context, MainActivity3::class.java)
        context.startActivity(intent)
    }
}





