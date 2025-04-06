package com.example.kotlinapp

import android.widget.Toast
import android.app.Activity
import android.util.Log // Import Log để sử dụng cho việc debug
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseUser

@Composable
fun LoginScreen(
    googleSignInClient: GoogleSignInClient,
    firebaseAuth: FirebaseAuth,
    onLoginSuccess: (GoogleSignInAccount) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity // Ép kiểu thành Activity
    val auth = FirebaseAuth.getInstance()

    var isLoading by remember { mutableStateOf(false) }

    // Định nghĩa launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            isLoading = false
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                if (account != null) {
                    // Firebase Authentication
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                onLoginSuccess(account)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Login failed: ${authTask.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            } catch (e: ApiException) {
                Toast.makeText(
                    context,
                    "Google Sign-In Failed: ${e.statusCode}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    // Kiểm tra trạng thái đăng nhập của người dùng trước khi mở màn hình đăng nhập
    val currentUser: FirebaseUser? = firebaseAuth.currentUser
    if (currentUser != null) {
        // Người dùng đã đăng nhập, lấy thông tin GoogleSignInAccount
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context) // Sử dụng Activity thay vì Context
        if (googleSignInAccount != null) {
            // In thông tin người dùng ra Logcat (terminal)
            Log.d("UserInfo", "UID: ${currentUser.uid}")
            Log.d("UserInfo", "Display Name: ${currentUser.displayName}")
            Log.d("UserInfo", "Email: ${currentUser.email}")
            Log.d("UserInfo", "Photo URL: ${currentUser.photoUrl}")

            // Gọi onLoginSuccess với GoogleSignInAccount
            onLoginSuccess(googleSignInAccount)
        } else {
            // Nếu không có thông tin GoogleSignInAccount, yêu cầu đăng nhập lại
            Toast.makeText(context, "Google Sign-In required again", Toast.LENGTH_SHORT).show()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.uth_logo),
            contentDescription = "UTH Logo",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "SmartTasks",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color(0xFF0084FF)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Welcome\nReady to explore? Log in to get started.",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    isLoading = true
                    // Đảm bảo đăng xuất trước khi đăng nhập lại
                    firebaseAuth.signOut()

                    // Mở màn hình đăng nhập Google
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign In with Google")
            }
        }
    }
}
