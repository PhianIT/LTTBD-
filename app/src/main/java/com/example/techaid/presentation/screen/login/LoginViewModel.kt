package com.example.techaid.presentation.screen.login

import androidx.lifecycle.ViewModel
import com.example.techaid.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String?) : LoginUiState()
}

class LoginViewModel : ViewModel() {
    private val repository = AuthRepository()
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> get() = _uiState

    fun login(email: String, password: String) {
        _uiState.value = LoginUiState.Loading
        repository.login(email, password) { success, message ->
            _uiState.value = if (success) LoginUiState.Success
            else LoginUiState.Error(message)
        }
    }
}
