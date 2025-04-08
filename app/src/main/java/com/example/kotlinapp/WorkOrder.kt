package com.example.kotlinapp.model
import com.google.firebase.Timestamp

data class WorkOrder(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val status: String = "",
    val due_date: Timestamp? = null,
    val assigned_to: String = "",
    val created_at: Timestamp? = null
)
