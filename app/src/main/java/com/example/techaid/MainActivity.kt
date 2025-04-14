package com.example.techaid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.techaid.navigation.NavGraph
import com.example.techaid.ui.theme.TechAidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TechAidTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
