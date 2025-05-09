// MainScreen.kt
package com.example.kotlinapp.ui.screen

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kotlinapp.layout.AppBottomNavigation
import com.example.kotlinapp.layout.AppTopBar
import com.example.kotlinapp.layout.BottomNavItem
import com.example.kotlinapp.ui.screen.schedule.ScheduleScreen
import com.example.kotlinapp.screens.WorkOrderScreen
import com.example.kotlinapp.ui.screen.assets.AssetScreen
import com.example.kotlinapp.ui.screen.inventory.InventoryScreen
import com.google.firebase.auth.FirebaseAuth

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
                },
                onNotificationClick = {
                    navController.navigate("notifications")
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
                AssetScreen()
            }
            composable("schedule") {
                ScheduleScreen()
            }
            composable("inventory") {
                InventoryScreen()
            }
            composable("work_order_detail/{orderId}") { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId")
                WorkOrderDetailScreen(orderId = orderId)
            }
            composable("profile/{userId}") { val firebaseUser = FirebaseAuth.getInstance().currentUser
                val email = firebaseUser?.email ?: "Không có email"
                val name = firebaseUser?.displayName ?: "Không có tên"

                ProfileScreen(
                    email = email,
                    name = name,
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo("profile/{userId}") { inclusive = true }
                        }
                    }
                )
            }

        }
    }
}

