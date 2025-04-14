package com.example.techaid.data.model

data class User(
    val uid: String = "",
    val email: String = "",
    val fullName: String = "",
    val avatarUrl: String = "",
    val role: String = "user",
    val createdAt: Long = System.currentTimeMillis()
)
