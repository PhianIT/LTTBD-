package com.example.kotlinapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinapp.model.WorkOrder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WorkOrdersViewModel : ViewModel() {
    private val _workOrders = MutableStateFlow<List<WorkOrder>>(emptyList())
    val workOrders: StateFlow<List<WorkOrder>> = _workOrders

    init {
        fetchWorkOrders()
    }

    // 🔄 Lấy danh sách WorkOrder từ Firestore
    fun fetchWorkOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = FirebaseFirestore.getInstance()
                val result = db.collection("work_orders").get().await()

                val list = result.map { doc ->
                    WorkOrder(
                        id = doc.getString("id") ?: doc.id, // fallback nếu không có trường id
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        assigned_to = doc.getString("assigned_to") ?: "",
                        created_at = doc.getTimestamp("created_at"),
                        due_date = doc.getTimestamp("due_date"),
                        status = doc.getString("status") ?: ""
                    )
                }
                _workOrders.value = list
            } catch (e: Exception) {
                // TODO: Log lỗi hoặc xử lý lỗi theo cách của bạn
                e.printStackTrace()
            }
        }
    }

    // ➕ Thêm WorkOrder mới
    fun addWorkOrder(workOrder: WorkOrder) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = FirebaseFirestore.getInstance()
                val newDoc = db.collection("work_orders").document()
                val data = hashMapOf(
                    "id" to newDoc.id,
                    "title" to workOrder.title,
                    "description" to workOrder.description,
                    "assigned_to" to workOrder.assigned_to,
                    "created_at" to workOrder.created_at,
                    "due_date" to workOrder.due_date,
                    "status" to workOrder.status
                )
                newDoc.set(data).await()
                fetchWorkOrders()  // Cập nhật danh sách sau khi thêm
            } catch (e: Exception) {
                // TODO: Log lỗi hoặc xử lý lỗi theo cách của bạn
                e.printStackTrace()
            }
        }
    }

    // ✏️ Cập nhật WorkOrder
    fun updateWorkOrder(workOrder: WorkOrder) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = FirebaseFirestore.getInstance()
                val data = hashMapOf(
                    "id" to workOrder.id,
                    "title" to workOrder.title,
                    "description" to workOrder.description,
                    "assigned_to" to workOrder.assigned_to,
                    "created_at" to workOrder.created_at,
                    "due_date" to workOrder.due_date,
                    "status" to workOrder.status
                )
                db.collection("work_orders").document(workOrder.id).set(data).await()
                fetchWorkOrders()  // Cập nhật danh sách sau khi sửa
            } catch (e: Exception) {
                // TODO: Log lỗi hoặc xử lý lỗi theo cách của bạn
                e.printStackTrace()
            }
        }
    }

    // ❌ Xóa WorkOrder theo ID
    fun deleteWorkOrder(workOrderId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = FirebaseFirestore.getInstance()
                db.collection("work_orders").document(workOrderId).delete().await()
                fetchWorkOrders()  // Cập nhật danh sách sau khi xóa
            } catch (e: Exception) {
                // TODO: Log lỗi hoặc xử lý lỗi theo cách của bạn
                e.printStackTrace()
            }
        }
    }
}
