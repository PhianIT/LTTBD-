package com.example.kotlinapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

@Composable
fun AppBottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Lệnh làm việc", Icons.Default.List, "work_orders"),
        BottomNavItem("Tài sản", Icons.Default.Star, "assets"),
        BottomNavItem("Lịch trình", Icons.Default.DateRange, "schedule"),
        BottomNavItem("Hạng mục kho", Icons.Default.Edit, "inventory")
    )

    NavigationBar( // Thay BottomNavigation bằng NavigationBar
        containerColor = Color.White,
        contentColor = Color.Gray
    ){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem( // Thay BottomNavigationItem bằng NavigationBarItem
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF1E88E5),
                    selectedTextColor = Color(0xFF1E88E5),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}


