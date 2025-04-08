//package com.example.kotlinapp
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.compose.setContent
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.firebase.auth.FirebaseAuth
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var googleSignInClient: GoogleSignInClient
//    private lateinit var firebaseAuth: FirebaseAuth
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Configure Google Sign-In
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//        firebaseAuth = FirebaseAuth.getInstance()
//
//        setContent {
//            LoginScreen(
//                googleSignInClient = googleSignInClient,
//                firebaseAuth = firebaseAuth,
//                onLoginSuccess = { account ->
//                    // Chuyển đến màn hình tiếp theo khi đăng nhập thành công
//                    val intent = Intent(this, ProfileActivity::class.java)
//                    intent.putExtra("name", account.displayName)
//                    intent.putExtra("email", account.email)
//                    startActivity(intent)
//                }
//            )
//        }
//    }
//}

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
import androidx.compose.material3.FabPosition
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // Khởi tạo Firebase
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    // Tạo navController để điều hướng giữa các màn hình
    val navController = rememberNavController()

    // Lấy route hiện tại để hiển thị tiêu đề phù hợp trên top bar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Danh sách các item trong bottom navigation
    val items = listOf(
        BottomNavItem("Lệnh làm việc", Icons.Default.List, "work_orders"),
        BottomNavItem("Tài sản", Icons.Default.Star, "assets"),
        BottomNavItem("Lịch trình", Icons.Default.DateRange, "schedule"),
        BottomNavItem("Hạng mục kho", Icons.Default.Edit, "inventory")
    )

    // Tiêu đề hiển thị trên top bar, sẽ thay đổi theo route
    val currentTitle = items.find { it.route == currentRoute }?.label ?: "Lệnh làm việc"

    // Scaffold để tạo cấu trúc UI với top bar, bottom bar và content
    Scaffold(
        topBar = { AppTopBar(title = currentTitle) },
        bottomBar = { AppBottomNavigation(navController) }
    ) { innerPadding ->
        // NavHost sẽ chứa các màn hình và xác định các route
        NavHost(
            navController = navController,
            startDestination = "work_orders", // Đặt màn hình mặc định là "work_orders"
            modifier = Modifier.padding(innerPadding)
        ) {
            // Định nghĩa các route và các composable màn hình tương ứng
            composable("work_orders") {
                WorkOrderScreen() // Màn hình cho route "work_orders"
            }
            composable("assets") {
                val themeViewModel: ThemeViewModel = viewModel()
                ThemeScreen()
            // Màn hình cho route "assets"
            }
            composable("schedule") {
//                ScheduleScreen() // Màn hình cho route "schedule"
            }
            composable("inventory") {
//                InventoryScreen() // Màn hình cho route "inventory"
            }
        }
    }
}


