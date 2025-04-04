package com.example.kotlinapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppContent(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "work_orders",
        modifier = modifier
    ) {
        composable("work_orders") {
            PlaceholderScreen()
        }
        composable("assets") {
            PlaceholderScreen()
        }
        composable("schedule") {
            PlaceholderScreen()
        }
        composable("inventory") {
            PlaceholderScreen()
        }
    }
}

@Composable
fun PlaceholderScreen() {
    Text(
        text = "Page not found!",
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.headlineSmall
    )
}