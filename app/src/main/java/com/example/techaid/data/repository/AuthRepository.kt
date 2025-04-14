package com.example.techaid.data.repository

import com.example.techaid.data.model.User
import com.example.techaid.firebase.AuthService

class AuthRepository {
    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        AuthService.login(email, password, onResult)
    }

    fun register(user: User, password: String, onResult: (Boolean, String?) -> Unit) {
        AuthService.registerUser(user.email, password, user, onResult)
    }

    fun logout() {
        AuthService.logout()
    }
}
