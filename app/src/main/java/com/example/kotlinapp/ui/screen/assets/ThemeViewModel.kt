package com.example.kotlinapp.ui.screen.assets

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.kotlinapp.ui.screen.inventory.Inventory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _assetList = MutableStateFlow<List<Asset>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    val assetList: StateFlow<List<Asset>> = _assetList
    val searchQuery: StateFlow<String> = _searchQuery
    val filteredAssetList = kotlinx.coroutines.flow.combine(_assetList, _searchQuery) { assets, query ->
        if (query.isBlank()) assets
        else assets.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.code.contains(query, ignoreCase = true)
        }
    }

    init {
        fetchAssets()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // Lấy danh sách tài sản từ Firestore
    fun fetchAssets() {
        db.collection("assets")
            .get()
            .addOnSuccessListener { result ->
                val list = result.mapNotNull { it.toObject(Asset::class.java) }
                _assetList.value = list
                Log.d("Firestore", "Tải thành công: ${list.size} mục")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Lỗi khi đọc dữ liệu: ${e.message}")
            }
    }

    // Thêm tài sản mới vào Firestore
    fun addAsset(asset: Asset) {
        db.collection("assets")
            .add(asset)
            .addOnSuccessListener {
                Log.d("Firestore", "Thêm thành công")
                fetchAssets()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Lỗi khi thêm: ${e.message}")
            }
    }
}
