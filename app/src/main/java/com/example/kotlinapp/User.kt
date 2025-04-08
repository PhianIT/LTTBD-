package com.example.kotlinapp.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "",         // ví dụ: "admin", "employee", "manager"
    val avatarUrl: String = ""     // đường dẫn hình đại diện (nếu có)
)