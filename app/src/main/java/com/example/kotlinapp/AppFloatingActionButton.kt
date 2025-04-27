package com.example.kotlinapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppFloatingActionButton() {
    FloatingActionButton(
        onClick = { /* Xử lý khi nhấn nút "+" */ },
//        backgroundColor = Color(0xFF1E88E5)
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
    }
}