package com.example.kotlinapp.ui.screen.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kotlinapp.R
import com.example.kotlinapp.ui.theme.AppBlue
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.example.kotlinapp.util.SharedPreferencesHelper


@Composable
fun SplashScreen(navController: NavController) {
    var visible by remember { mutableStateOf(false) }
    val scaleAnim = remember { Animatable(0.8f) }
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()

    LaunchedEffect(true) {
        visible = true
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = EaseOutBack)
        )

        delay(2000)

        // ✅ Đọc từ SharedPreferences thay vì Firebase
        val email = SharedPreferencesHelper.getUserEmail(context)
        val name = SharedPreferencesHelper.getUserName(context)

        if (!email.isNullOrEmpty() && !name.isNullOrEmpty()) {
            val encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString())
            val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())

            navController.navigate("main/$encodedEmail") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(1000)),
        exit = fadeOut(animationSpec = tween(500))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logo_tech_aid),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scaleAnim.value)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row {
                    Text(
                        text = "Tech",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Aid",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppBlue
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Chào mừng bạn đến với ứng dụng",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
