package com.example.kotlinapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.kotlinapp.ui.navigation.NavGraph
import com.example.kotlinapp.util.createGoogleSignInClient


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
            val navController = rememberNavController()
            val firebaseAuth = FirebaseAuth.getInstance()
            val googleSignInClient = remember { createGoogleSignInClient(this) }

            NavGraph(
                navController = navController,
                firebaseAuth = firebaseAuth,
                googleSignInClient = googleSignInClient
            )
        }
    }
}
