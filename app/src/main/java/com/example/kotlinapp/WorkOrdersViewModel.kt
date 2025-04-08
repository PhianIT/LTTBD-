package com.example.kotlinapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.kotlinapp.model.WorkOrder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WorkOrdersViewModel : ViewModel() {
    private val _workOrders = MutableStateFlow<List<WorkOrder>>(emptyList())
    val workOrders: StateFlow<List<WorkOrder>> = _workOrders

    init {
        fetchWorkOrders()
    }

    private fun fetchWorkOrders() {
        val db = FirebaseFirestore.getInstance()
        db.collection("work_orders")
            .get()
            .addOnSuccessListener { result ->
                val list = result.map { doc ->
                    WorkOrder(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: ""
                    )
                }
                _workOrders.value = list
            }
            .addOnFailureListener {
                // TODO: handle error
            }
    }
}
