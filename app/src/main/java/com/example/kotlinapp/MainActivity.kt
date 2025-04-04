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
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
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
    val navController = rememberNavController()

    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = { AppBottomNavigation(navController) },
        floatingActionButton = { AppFloatingActionButton() },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        AppContent(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

