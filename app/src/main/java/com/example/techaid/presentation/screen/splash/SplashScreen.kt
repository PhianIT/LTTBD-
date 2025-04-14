package com.example.techaid.presentation.screen.splash

import androidx.compose.runtime.*
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.techaid.core.constants.AppConstants
import com.example.techaid.navigation.Screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.techaid.R
import com.example.techaid.ui.theme.PrimaryBlue
import com.example.techaid.ui.theme.TextDark

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(true) {
        delay(AppConstants.SPLASH_DURATION)
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 🔵 Logo hình tròn
            Image(
                painter = painterResource(id = R.drawable.ic_logo), // bạn cần đổi tên file ảnh thành `logo.png` và để trong `res/drawable`
                contentDescription = "Logo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🆔 TechAid với màu tách biệt
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = TextDark, fontSize = 50.sp, fontWeight = FontWeight.Bold)) {
                        append("Tech")
                    }
                    withStyle(style = SpanStyle(color = PrimaryBlue, fontSize = 50.sp, fontWeight = FontWeight.Bold)) {
                        append("Aid")
                    }
                }
            )


            Spacer(modifier = Modifier.height(8.dp))

            // 👋 Dòng chào
            Text(
                text = "Chào mừng bạn đến với ứng dụng",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp,
                color = TextDark
            )
        }
    }
}
