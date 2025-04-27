package com.example.kotlinapp.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _selectedColor = MutableStateFlow("#FFFFFF") // mặc định trắng
    val selectedColor: StateFlow<String> = _selectedColor

    fun setSelectedColor(color: String) {
        _selectedColor.value = color
    }

    fun saveColorToFirebase() {
        db.collection("app_theme").document("theme_color")
            .set(mapOf("color" to _selectedColor.value))
    }

    fun fetchThemeColor() {
        db.collection("app_theme").document("theme_color").get()
            .addOnSuccessListener { document ->
                val color = document.getString("color")
                color?.let { _selectedColor.value = it }
            }
    }
}
