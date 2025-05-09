package com.example.kotlinapp.ui.screen.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun AssetScreen() {
    val viewModel: ThemeViewModel = viewModel()
    val assetList by viewModel.assetList.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchAssets()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(assetList) { asset ->
                AssetCard(asset = asset)
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
            Icon(Icons.Default.Add, contentDescription = "Thêm tài sản")
        }

        // ✅ Hiển thị Dialog khi showDialog = true
        if (showDialog) {
            com.example.kotlinapp.ui.screen.inventory.AddAssetDialog(
                onDismiss = { showDialog = false },
                onSave = { newAsset ->
                    viewModel.addAsset(newAsset)
                    viewModel.fetchAssets()
                    showDialog = false
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssetDialog(
    onDismiss: () -> Unit,
    onSave: (Asset) -> Unit
) {
    var code by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Có sẵn") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFF3EBFF)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Thêm tài sản mới", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Mã tài sản") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên tài sản") })
                Spacer(Modifier.height(8.dp))
                var expanded by remember { mutableStateOf(false) }
                val statusOptions = listOf("Có sẵn", "Sự cố")

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = status,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Trạng thái") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        statusOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    status = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

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
                        val asset = Asset(
                            code = code,
                            name = name,
                            status = status,
                            id = System.currentTimeMillis().toString(),
                            time = com.example.kotlinapp.ui.screen.inventory.getCurrentTime()
                        )
                        onSave(asset)
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
fun AssetCard(asset: Asset) {
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
                Text(asset.code, fontWeight = FontWeight.Bold)
                com.example.kotlinapp.ui.screen.inventory.StatusBadge(asset.status)
                Text("Tiêu đề: ${asset.name}")
                Text("Mẫu: ${asset.id}")
                Text("Ngày&Giờ: ${asset.time}")
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
