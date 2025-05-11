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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlinapp.layout.AppBottomNavigation
import com.example.kotlinapp.layout.AppTopBar
import com.example.kotlinapp.layout.BottomNavItem
import com.example.kotlinapp.screens.WorkOrderScreen
import com.example.kotlinapp.ui.screen.assets.AssetScreen
import com.example.kotlinapp.ui.screen.inventory.InventoryScreen
import com.example.kotlinapp.ui.screen.schedule.ScheduleScreen
import com.example.kotlinapp.util.SharedPreferencesHelper
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    userId: String = "user_001",
    parentNavController: NavHostController // Truyền từ NavGraph
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

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
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    val email = firebaseUser?.email ?: SharedPreferencesHelper.getUserEmail(context) ?: "Không có email"
                    val name = firebaseUser?.displayName ?: SharedPreferencesHelper.getUserName(context) ?: "Không có tên"
                    navController.navigate("profile/$email/$name")
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

            composable(
                "profile/{email}/{name}",
                arguments = listOf(
                    navArgument("email") { type = NavType.StringType },
                    navArgument("name") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email") ?: "Không có email"
                val name = backStackEntry.arguments?.getString("name") ?: "Không có tên"

                ProfileScreen(
                    email = email,
                    name = name,
                    onLogout = {
                        FirebaseAuth.getInstance().signOut()
                        SharedPreferencesHelper.clearUser(context)
                        parentNavController.navigate("login") {
                            popUpTo("main/$userId") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
