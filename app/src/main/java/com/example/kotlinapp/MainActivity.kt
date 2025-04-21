package com.example.kotlinapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.kotlinapp.screens.WorkOrderScreen
import com.example.kotlinapp.ui.ThemeScreen
import com.example.kotlinapp.viewmodel.ThemeViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.example.kotlinapp.ui.screen.login.LoginScreen
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // Khởi tạo Firebase
        FirebaseAuth.getInstance().signOut()
        setContent {
            val navController = rememberNavController()
            val context = this

            // Firebase Auth
            val firebaseAuth = FirebaseAuth.getInstance()

            // Cấu hình Google Sign-In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Đảm bảo bạn đã cấu hình trong Firebase Console
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)

            val startDestination = if (firebaseAuth.currentUser != null) "home" else "login"

            NavHost(navController = navController, startDestination = startDestination) {
                composable("login") {
                    LoginScreen(
                        googleSignInClient = googleSignInClient,
                        firebaseAuth = firebaseAuth,
                        onLoginSuccess = { email, name ->
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onSignUpClick = {
                            // Điều hướng đến màn hình đăng ký nếu bạn có
                        }
                    )
                }

                composable("home") {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
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
        topBar = { AppTopBar(title = currentTitle) },
        bottomBar = { AppBottomNavigation(navController) }
    ) { innerPadding ->

        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email ?: "Không rõ"
        val name = user?.displayName ?: "Không rõ"

        NavHost(
            navController = navController,
            startDestination = "work_orders",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("work_orders") {
                WorkOrderScreen(
                    email = email,
                    name = name,
                    onLogout = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onViewDetail = { workOrder ->
                        // TODO: Điều hướng đến màn hình chi tiết work order
                        // Ví dụ: navController.navigate("detail/${workOrder.id}")
                    },
                    onEdit = { workOrder ->
                        // TODO: Điều hướng đến màn hình chỉnh sửa work order
                        // Ví dụ: navController.navigate("edit/${workOrder.id}")
                    }
                )
            }

            composable("assets") {
                val themeViewModel: ThemeViewModel = viewModel()
                ThemeScreen()
            }

            composable("schedule") {
                // ScheduleScreen()
            }

            composable("inventory") {
                // InventoryScreen()
            }
        }
    }
}
