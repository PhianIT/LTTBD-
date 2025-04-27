package com.example.kotlinapp.notification

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.workorder.WorkOrdersViewModel
import java.util.*
import java.util.concurrent.TimeUnit
@Composable
fun NotificationMenu(viewModel: WorkOrdersViewModel = viewModel()) {
    val allWorkOrders by viewModel.workOrders.collectAsState()
    val now = Date()
    val threeDaysLater = Date(now.time + 3 * 24 * 60 * 60 * 1000)

    val upcomingWorkOrders = allWorkOrders.filter {
        val dueDate = it.due_date?.toDate()
        dueDate != null && dueDate.after(now) && dueDate.before(threeDaysLater)
    }

    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.Email, contentDescription = "Thông báo")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (upcomingWorkOrders.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("Không có công việc đến hạn") },
                    onClick = {}
                )
            } else {
                upcomingWorkOrders.forEach { workOrder ->
                    val dueDate = workOrder.due_date?.toDate()
                    val timeLeft = dueDate?.time?.minus(now.time) ?: 0L
                    val days = TimeUnit.MILLISECONDS.toDays(timeLeft)
                    val hours = TimeUnit.MILLISECONDS.toHours(timeLeft) % 24
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60

                    DropdownMenuItem(
                        text = {
                            Column {
                                Text("📌 ${workOrder.title}")
                                Text("⏰ ${days}d ${hours}h ${minutes}m còn lại", style = MaterialTheme.typography.bodySmall)
                            }
                        },
                        onClick = {
                            // Tuỳ chọn xử lý nếu muốn điều hướng đến chi tiết
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
