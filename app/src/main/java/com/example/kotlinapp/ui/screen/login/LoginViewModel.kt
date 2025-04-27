// File: ui/screen/login/LoginViewModel.kt
package com.example.kotlinapp.ui.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val userId: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState

    fun loginWithEmail(email: String, password: String) {
        _loginState.value = LoginUiState.Loading
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _loginState.value = if (task.isSuccessful) {
                        LoginUiState.Success(task.result?.user?.uid ?: "")
                    } else {
                        LoginUiState.Error(task.exception?.localizedMessage ?: "Đăng nhập thất bại")
                    }
                    Log.e("LoginViewModel", "Login failed", task.exception)
                }
        }
    }

    fun resetState() {
        _loginState.value = LoginUiState.Idle
    }
}
