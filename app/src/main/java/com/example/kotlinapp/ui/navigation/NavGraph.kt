// NavGraph.kt
package com.example.kotlinapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.kotlinapp.ui.screen.login.LoginScreen
import com.example.kotlinapp.ui.screen.signup.SignUpScreen
import com.example.kotlinapp.ui.screen.splash.SplashScreen
import com.example.kotlinapp.screens.MainScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    firebaseAuth: FirebaseAuth,
    googleSignInClient: GoogleSignInClient
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "splash",
        enterTransition = {
            scaleIn(initialScale = 0.8f, animationSpec = tween(500))
        },
        exitTransition = {
            scaleOut(targetScale = 1.1f, animationSpec = tween(300))
        },
        popEnterTransition = {
            scaleIn(initialScale = 0.8f, animationSpec = tween(500))
        },
        popExitTransition = {
            scaleOut(targetScale = 1.1f, animationSpec = tween(300))
        }
    ) {

        composable("splash") {
            SplashScreen(navController = navController)
        }

        composable("login") {
            LoginScreen(
                googleSignInClient = googleSignInClient,
                firebaseAuth = firebaseAuth,
                onLoginSuccess = { email, name ->
                    val encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString())
                    navController.navigate("main/$encodedEmail") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate("signup")
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                googleSignInClient = googleSignInClient,
                navController = navController,
                firebaseAuth = firebaseAuth,
                onLoginSuccess = { email, name ->
                    val encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString())
                    navController.navigate("main/$encodedEmail") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }

        // ProfileScreen có thể vẫn giữ nếu bạn cần gọi nó từ MainScreen
        composable(
            "profile/{email}/{name}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            com.example.kotlinapp.ui.screen.profile.ProfileScreen(
                email = email,
                name = name,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("profile/{email}/{name}") { inclusive = true }
                    }
                }
            )
        }

        composable(
            "main/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: "user_001"
            MainScreen(userId = userId)
        }
    }
}
