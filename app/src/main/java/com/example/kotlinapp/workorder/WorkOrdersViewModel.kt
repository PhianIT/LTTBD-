package com.example.kotlinapp.workorder

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WorkOrdersViewModel : ViewModel() {
    private val _workOrders = MutableStateFlow<List<WorkOrder>>(emptyList())
    val workOrders: StateFlow<List<WorkOrder>> = _workOrders

    init {
        fetchWorkOrders()
    }

//    private fun fetchWorkOrders() {
//        val db = FirebaseFirestore.getInstance()
//        db.collection("work_orders")
//            .get()
//            .addOnSuccessListener { result ->
//                val list = result.map { doc ->
//                    WorkOrder(
//                        id = doc.id, // <-- Đây mới đúng là documentId
//                        title = doc.getString("title") ?: "",
//                        description = doc.getString("description") ?: "",
//                        assigned_to = doc.getString("assigned_to") ?: "",
//                        created_at = doc.getTimestamp("created_at"),
//                        due_date = doc.getTimestamp("due_date"),
//                        status = doc.getString("status") ?: "",
//                        imageUrl = doc.getString("imageUrl")
//                    )
//                }
//                _workOrders.value = list
//            }
//            .addOnFailureListener {
//                // TODO: handle error
//            }
//    }
private fun fetchWorkOrders() {
    val db = FirebaseFirestore.getInstance()
    db.collection("work_orders")
        .addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("WorkOrders", "Lỗi khi lắng nghe cập nhật", error)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val list = snapshot.documents.map { doc ->
                    WorkOrder(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        assigned_to = doc.getString("assigned_to") ?: "",
                        created_at = doc.getTimestamp("created_at"),
                        due_date = doc.getTimestamp("due_date"),
                        status = doc.getString("status") ?: "",
                        imageUrl = doc.getString("imageUrl")
                    )
                }
                _workOrders.value = list
            }
        }
}

    fun addWorkOrder(workOrder: WorkOrder) {
        val db = FirebaseFirestore.getInstance()
        val data = hashMapOf(
            "title" to workOrder.title,
            "description" to workOrder.description,
            "assigned_to" to workOrder.assigned_to,
            "created_at" to workOrder.created_at,
            "due_date" to workOrder.due_date,
            "status" to "assigned",
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
    fun deleteWorkOrder(documentId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("work_orders")
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                // Xoá thành công
                fetchWorkOrders() // Cập nhật lại danh sách
            }
            .addOnFailureListener { e ->
                // Xử lý lỗi
                Log.e("DeleteWorkOrder", "Lỗi khi xoá: ${e.message}")
            }
    }
    fun fetchWorkOrderById(orderId: String, onResult: (WorkOrder?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("work_orders").document(orderId).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val order = WorkOrder(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        assigned_to = doc.getString("assigned_to") ?: "",
                        created_at = doc.getTimestamp("created_at"),
                        due_date = doc.getTimestamp("due_date"),
                        status = doc.getString("status") ?: "",
                    )
                    onResult(order)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun updateWorkOrder(order: WorkOrder) {
        val db = FirebaseFirestore.getInstance()
        db.collection("work_orders")
            .document(order.id)
            .set(order)
            .addOnSuccessListener { Log.d("Update", "Cập nhật thành công") }
            .addOnFailureListener { e -> Log.e("Update", "Lỗi: ${e.message}") }
    }


}

