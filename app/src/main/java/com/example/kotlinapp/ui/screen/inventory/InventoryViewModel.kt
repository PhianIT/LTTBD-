package com.example.kotlinapp.ui.screen.inventory

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InventoryViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _inventoryList = MutableStateFlow<List<Inventory>>(emptyList())
    val inventoryList: StateFlow<List<Inventory>> = _inventoryList

    // Lấy danh sách tài sản từ Firestore
    fun fetchInventory() {
        db.collection("inventory")
            .get()
            .addOnSuccessListener { result ->
                val list = result.mapNotNull { it.toObject(Inventory::class.java) }
                _inventoryList.value = list
                Log.d("Firestore", "Tải thành công: ${list.size} mục")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Lỗi khi đọc dữ liệu: ${e.message}")
            }
    }

    // Thêm tài sản mới vào Firestore
    fun addInventory(inventory: Inventory) {
        db.collection("inventory")
            .add(inventory)
            .addOnSuccessListener {
                Log.d("Firestore", "Thêm thành công")
                fetchInventory()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Lỗi khi thêm: ${e.message}")
            }
    }
}
