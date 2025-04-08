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
                        id = doc.getString("id") ?: "",
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        assigned_to = doc.getString("assigned_to") ?: "",
                        created_at = doc.getTimestamp("created_at"),
                        due_date = doc.getTimestamp("due_date"),
                        status = doc.getString("status") ?: ""
                    )
                }
                _workOrders.value = list
            }
            .addOnFailureListener {
                // TODO: handle error
            }
    }

    fun addWorkOrder(workOrder: WorkOrder) {
        val db = FirebaseFirestore.getInstance()
        val data = hashMapOf(
            "title" to workOrder.title,
            "description" to workOrder.description,
            "assigned_to" to workOrder.assigned_to,
            "created_at" to workOrder.created_at,
            "due_date" to workOrder.due_date
        )
        db.collection("work_orders")
            .add(data)
            .addOnSuccessListener {
                fetchWorkOrders() // Refresh danh sách
            }
            .addOnFailureListener {
                // TODO: Xử lý lỗi
            }
    }

}

