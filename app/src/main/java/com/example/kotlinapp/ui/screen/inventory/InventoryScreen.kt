package com.example.kotlinapp.ui.screen.inventory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.R
import androidx.compose.foundation.lazy.items

@Composable
fun InventoryScreen() {
    val viewModel: InventoryViewModel = viewModel()
    val inventoryList by viewModel.inventoryList.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchInventory()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(inventoryList) { inventory ->
                InventoryCard(inventory = inventory)
            }
        }

        // ✅ Floating Action Button ở góc dưới phải
        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Thêm hàng")
        }

        // ✅ Hiển thị Dialog khi showDialog = true
        if (showDialog) {
            AddAssetDialog(
                onDismiss = { showDialog = false },
                onSave = { newInventory ->
                    viewModel.addInventory(newInventory)
                    viewModel.fetchInventory()
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AddAssetDialog(
    onDismiss: () -> Unit,
    onSave: (Inventory) -> Unit
) {
    var code by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var all by remember { mutableStateOf("") }


    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFF3EBFF)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Thêm hàng", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Mã tài sản") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên tài sản") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = all, onValueChange = { all = it }, label = { Text("Chi tiết") })

                Spacer(Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val inventory = Inventory(
                            code = code,
                            name = name,
                            id = System.currentTimeMillis().toString(),
                            time = getCurrentTime()
                        )
                        onSave(inventory)
                    }) {
                        Text("Lưu")
                    }
                }
            }
        }
    }
}
fun getCurrentTime(): String {
    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date())
}


@Composable
fun InventoryCard(inventory: Inventory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.uth_logo),
                contentDescription = "Asset Image",
                modifier = Modifier.size(60.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(inventory.code, fontWeight = FontWeight.Bold)
                Text("Tiêu đề: ${inventory.name}")
                Text("Mẫu: ${inventory.id}")
                Text("Chi tiết: ${inventory.all}")
                Text("Ngày&Giờ: ${inventory.time}")

            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val color = when (status) {
        "Có sẵn" -> Color(0xFF4CAF50)
        "Sự cố" -> Color(0xFFF44336)
        else -> Color.Gray
    }

    Text(
        text = status,
        color = Color.White,
        modifier = Modifier
            .background(color = color, shape = MaterialTheme.shapes.small)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        style = MaterialTheme.typography.labelSmall
    )
}
