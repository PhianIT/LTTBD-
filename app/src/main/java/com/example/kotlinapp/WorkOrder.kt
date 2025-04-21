package com.example.kotlinapp.model


data class WorkOrder(

    val id: String = "",
    val title: String = "",
    val description: String = "",
    val assigned_to: String = "",
    val created_at: com.google.firebase.Timestamp? = null,
    val due_date: com.google.firebase.Timestamp? = null,
    val status: String = ""
)
