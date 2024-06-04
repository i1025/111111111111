package com.example.a111111111111

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.a111111111111.ui.theme._111111111111Theme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.compose.ui.unit.dp
import com.example.a111111111111.ui.theme._111111111111Theme
import com.example.a111111111111.MainActivity2
import com.example.a111111111111.ui.theme._111111111111Theme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseFirestore.getInstance()

        enableEdgeToEdge()
        setContent {
            _111111111111Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    account()
                }
            }
        }
    }
}

@Composable
fun account() {
    var userId by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()
    var msg by remember { mutableStateOf("") }
    val context = LocalContext.current
    var url by remember { mutableStateOf("https://console.firebase.google.com/u/0/project/new20240605/firestore/databases/-default-/data/~2Fusers2~2F920724") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = userId,
            onValueChange = { newText -> userId = newText },
            label = { Text("帳號") },
            placeholder = { Text("請輸入您的帳號") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = userPassword,
            onValueChange = { newText -> userPassword = newText },
            label = { Text("密碼") },
            placeholder = { Text("請輸入您的密碼") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // 檢查帳號是否已經鎖定
            db.collection("員工")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val loginAttempts = document.getLong("loginAttempts") ?: 0
                        val isLocked = document.getBoolean("isLocked") ?: false
                        val storedPassword = document.getString("userPassword") ?: ""

                        if (isLocked) {
                            msg = "帳號已被鎖定，請聯絡客服"
                        } else if (userPassword == storedPassword) {
                            // 登入成功
                            db.collection("員工")
                                .document(userId)
                                .update(
                                    mapOf(
                                        "loginAttempts" to 0,
                                        "isLocked" to false
                                    )
                                )
                            msg = "登入成功"
                            val intent = Intent(context, MainActivity2::class.java)
                            context.startActivity(intent)
                        } else {
                            // 登入失敗
                            val newLoginAttempts = loginAttempts + 1
                            db.collection("員工")
                                .document(userId)
                                .update(
                                    mapOf(
                                        "loginAttempts" to newLoginAttempts,
                                        "isLocked" to (newLoginAttempts >= 3)
                                    )
                                )
                            msg = if (newLoginAttempts >= 3) {
                                "登入失敗：帳號已被鎖定"
                            } else {
                                "登入失敗：密碼錯誤"
                            }
                        }
                    } else {
                        msg = "帳號不存在"
                    }
                }
                .addOnFailureListener { e ->
                    msg = "登入失敗：" + e.toString()
                }
        }) {
            Text(text = "登入")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = msg)
    }
}




