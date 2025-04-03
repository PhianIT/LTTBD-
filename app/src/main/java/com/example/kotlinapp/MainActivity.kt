package com.example.kotlinapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        setContent {
            LoginScreen(
                googleSignInClient = googleSignInClient,
                firebaseAuth = firebaseAuth,
                onLoginSuccess = { account ->
                    // Chuyển đến màn hình tiếp theo khi đăng nhập thành công
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("name", account.displayName)
                    intent.putExtra("email", account.email)
                    startActivity(intent)
                }
            )
        }
    }
}
