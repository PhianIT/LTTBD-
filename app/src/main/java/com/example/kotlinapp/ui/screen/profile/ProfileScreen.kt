package com.example.kotlinapp.ui.screen.profile

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinapp.R
import com.example.kotlinapp.ui.theme.AppBlue
import com.google.firebase.auth.FirebaseAuth
import com.example.kotlinapp.util.SharedPreferencesHelper


@Composable
fun ProfileScreen(
    email: String,
    name: String,
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    // Lấy thông tin từ SharedPreferences nếu không được truyền vào
    val actualEmail = email ?: SharedPreferencesHelper.getUserEmail(context) ?: ""
    val actualName = name ?: SharedPreferencesHelper.getUserName(context) ?: ""


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Hồ sơ",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = AppBlue
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Avatar người dùng
        Box(contentAlignment = Alignment.BottomEnd) {
            Image(
                painter = painterResource(id = R.drawable.logo_tech_aid), // Thay bằng avatar mặc định
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            IconButton(onClick = {
                Toast.makeText(context, "Đổi ảnh đại diện (chưa hỗ trợ)", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = "Change Avatar",
                    tint = AppBlue,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tên người dùng
        OutlinedTextField(
            value = name,
            onValueChange = {},
            label = { Text("Tên") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = {},
            label = { Text("Email") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                SharedPreferencesHelper.clearUser(context)
                onLogout()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppBlue),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Đăng xuất", color = Color.White, fontSize = 16.sp)
        }
    }
}