package com.example.kotlinapp.user

import androidx.lifecycle.ViewModel
import com.example.kotlinapp.workorder.WorkOrder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

     internal fun getUserById(userId: String, onResult: (User?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("user").document(userId).get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    val user = User(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        email = doc.getString("email") ?: "",
                        phone = doc.getString("phone") ?: "",
                        role = doc.getString("role") ?: "",
                        avatarUrl = doc.getString("avatarUrl") ?: ""
                    )
                    onResult(user)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

}

