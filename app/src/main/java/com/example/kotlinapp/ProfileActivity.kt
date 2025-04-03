package com.example.kotlinapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lấy dữ liệu từ Intent
        val name = intent.getStringExtra("name") ?: "Unknown"
        val email = intent.getStringExtra("email") ?: "No email"

        // Sử dụng Jetpack Compose để hiển thị giao diện
        setContent {
            ProfileScreen(name = name, email = email)
        }
    }
}

@Composable
fun ProfileScreen(name: String, email: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_placeholder),
            contentDescription = "Profile Image",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị name và email, không cần lo về null vì đã xử lý ở onCreate
        Text(text = "Nammmmmmmmmmmmmmmmmmmme: $name", fontSize = 18.sp)
        Text(text = "Email: $email", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
        }) {
            Text("Back")
        }
    }
}