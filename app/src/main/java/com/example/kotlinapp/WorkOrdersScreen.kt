package com.example.kotlinapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.viewmodel.WorkOrdersViewModel
import com.example.kotlinapp.model.WorkOrder
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.*

@Composable
fun WorkOrderScreen(viewModel: WorkOrdersViewModel = viewModel()) {
    val workOrders by viewModel.workOrders.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Thêm Work Order")
            }
        }
    ) { padding ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(workOrders) { order ->
                    WorkOrderItem(order)
                }
            }

            if (showDialog) {
                AddWorkOrderDialog(
                    onDismiss = { showDialog = false },
                    onAdd = { workOrder ->
                        viewModel.addWorkOrder(workOrder)
                        showDialog = false
                    }
                )
            }
        }
    }
}


@Composable
fun WorkOrderItem(order: WorkOrder) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(order.id, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.width(8.dp))

                if (order.status.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .background(getStatusColor(order.status), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(getStatusText(order.status), color = Color.White, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Xem lệnh làm việc") },
                            onClick = { /* TODO */ }
                        )
                        DropdownMenuItem(
                            text = { Text("Chỉnh sửa") },
                            onClick = { /* TODO */ }
                        )
                        DropdownMenuItem(
                            text = { Text("Xoá") },
                            onClick = { /* TODO */ }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(buildAnnotatedString {
                append("Tiêu đề: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(order.title)
                }
            })

            Text(buildAnnotatedString {
                append("Mô tả: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(order.description)
                }
            })

            Text(buildAnnotatedString {
                append("Giao cho: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(order.assigned_to)
                }
            })

            Text(buildAnnotatedString {
                append("Ngày tạo: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(formatDate(order.created_at))
                }
            })

            Text(buildAnnotatedString {
                append("Hạn: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(formatDate(order.due_date))
                }
            }, fontSize = 12.sp)
        }
    }
}
@Composable
fun AddWorkOrderDialog(onDismiss: () -> Unit, onAdd: (WorkOrder) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var assignedTo by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Work Order mới") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Tiêu đề") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Mô tả") })
                OutlinedTextField(value = assignedTo, onValueChange = { assignedTo = it }, label = { Text("Giao cho") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val now = Timestamp.now()
                val workOrder = WorkOrder(
                    title = title,
                    description = description,
                    assigned_to = assignedTo,
                    created_at = now,
                    due_date = now // bạn có thể thay bằng date picker sau
                )
                onAdd(workOrder)
            }) {
                Text("Lưu")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}


fun formatDate(timestamp: Timestamp?): String {
    return timestamp?.toDate()?.let { date ->
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formatter.format(date)
    } ?: "Không rõ"
}

fun getStatusColor(status: String): Color {
    return when (status) {
        "in_progress" -> Color(0xFFB28DFF)
        "completed" -> Color(0xFF4CAF50)
        "assigned" -> Color(0xFF2196F3)
        else -> Color.Gray
    }
}

fun getStatusText(status: String): String {
    return when (status) {
        "in_progress" -> "Đang tiến hành"
        "completed" -> "Hoàn thành"
        "assigned" -> "Đã giao"
        else -> "Không rõ"
    }
}


