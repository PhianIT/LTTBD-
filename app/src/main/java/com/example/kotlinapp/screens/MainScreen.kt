// MainScreen.kt
package com.example.kotlinapp.screens

import WorkOrderDetailScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kotlinapp.layout.AppBottomNavigation
import com.example.kotlinapp.layout.AppTopBar
import com.example.kotlinapp.layout.BottomNavItem
import com.example.kotlinapp.schedule.ScheduleScreen
import com.example.kotlinapp.ui.ThemeScreen
import com.example.kotlinapp.user.ProfileScreen
import com.example.kotlinapp.viewmodel.ThemeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(userId: String = "user_001") {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem("Lệnh làm việc", Icons.Default.List, "work_orders"),
        BottomNavItem("Tài sản", Icons.Default.Star, "assets"),
        BottomNavItem("Lịch trình", Icons.Default.DateRange, "schedule"),
        BottomNavItem("Hạng mục kho", Icons.Default.Edit, "inventory")
    )

    val currentTitle = items.find { it.route == currentRoute }?.label ?: "Lệnh làm việc"

    Scaffold(
        topBar = {
            AppTopBar(
                title = currentTitle,
                onProfileClick = {
                    navController.navigate("profile/$userId")
                }
            )
        },
        bottomBar = { AppBottomNavigation(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "work_orders",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("work_orders") {
                WorkOrderScreen(navController = navController)
            }
            composable("assets") {
                val themeViewModel: ThemeViewModel = viewModel()
                ThemeScreen()
            }
            composable("schedule") {
                ScheduleScreen()
            }
            composable("inventory") {
                // InventoryScreen()
            }
            composable("work_order_detail/{orderId}") { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId")
                WorkOrderDetailScreen(orderId = orderId)
            }
            composable("profile/{userId}") { backStackEntry ->
//                val uid = backStackEntry.arguments?.getString("userId") ?: ""
                val uid ="user_001"
                ProfileScreen(userId = uid)
            }

        }
    }
}

