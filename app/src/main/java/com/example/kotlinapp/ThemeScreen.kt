package com.example.kotlinapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.viewmodel.ThemeViewModel

@Composable
fun ThemeScreen() {
    val viewModel: ThemeViewModel = viewModel()
    val selectedColor by viewModel.selectedColor.collectAsState()

    // Chuyển mã hex sang Color
    val backgroundColor = remember(selectedColor) {
        runCatching { Color(android.graphics.Color.parseColor(selectedColor)) }
            .getOrDefault(Color.White)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Chọn màu giao diện", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            ColorButton("#FFCDD2", viewModel)
            ColorButton("#C8E6C9", viewModel)
            ColorButton("#BBDEFB", viewModel)
            ColorButton("#FFF59D", viewModel)
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            viewModel.saveColorToFirebase()
        }) {
            Text("Apply")
        }
    }

    // Lấy màu khi mở màn hình
    LaunchedEffect(Unit) {
        viewModel.fetchThemeColor()
    }
}

@Composable
fun ColorButton(colorHex: String, viewModel: ThemeViewModel) {
    Button(
        onClick = { viewModel.setSelectedColor(colorHex) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(android.graphics.Color.parseColor(colorHex))
        ),
        modifier = Modifier.size(60.dp)
    ) {}
}
