package com.example.kotlinapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String) {
    TopAppBar(
        title = { Text(title) },
        actions = {
            IconButton(onClick = { /* Xử lý tìm kiếm */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
            IconButton(onClick = { /* Xử lý lịch */ }) {
                Icon(Icons.Default.DateRange, contentDescription = "Calendar")
            }
            IconButton(onClick = { /* Xử lý thông báo */ }) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Notifications")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF1E88E5),
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}
