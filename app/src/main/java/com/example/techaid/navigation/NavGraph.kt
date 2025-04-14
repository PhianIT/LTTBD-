package com.example.techaid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.techaid.presentation.screen.home.HomeScreen
import com.example.techaid.presentation.screen.login.LoginScreen
import com.example.techaid.presentation.screen.register.RegisterScreen
import com.example.techaid.presentation.screen.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Register.route) { RegisterScreen(navController) }
        composable(Screen.Home.route) { HomeScreen() }
    }
}
