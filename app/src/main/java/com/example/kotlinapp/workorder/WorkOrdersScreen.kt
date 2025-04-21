package com.example.kotlinapp.screens

import WorkOrderDetailScreen
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.workorder.WorkOrdersViewModel
import com.example.kotlinapp.workorder.WorkOrder
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.*
import android.app.DatePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavController
import com.google.firebase.storage.FirebaseStorage
import coil.compose.AsyncImage


@Composable
fun WorkOrderScreen(viewModel: WorkOrdersViewModel = viewModel(),navController: NavController) {
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
                    // Truyền navController vào cho WorkOrderItem
                    WorkOrderItem(order = order, navController = navController)
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
fun WorkOrderItem(order: WorkOrder,navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    val viewModel: WorkOrdersViewModel = viewModel()
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
                    println("status ${order.status}")
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
                            text = { Text("Chi Tiết") },
                            onClick = {
                                navController.navigate("work_order_detail/${order.id}")
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Chỉnh sửa") },
                            onClick = { /* TODO */ }
                        )
                        DropdownMenuItem(
                            text = { Text("Xoá") },
                            onClick = {
                                viewModel.deleteWorkOrder(order.id)
                                expanded = false
                            }
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
            },)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkOrderDialog(
    onDismiss: () -> Unit,
    onAdd: (WorkOrder) -> Unit
) {
    val context = LocalContext.current
    val storage = FirebaseStorage.getInstance()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var assignedTo by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var uploading by remember { mutableStateOf(false) }

    val now = remember { Calendar.getInstance() }
    var dueDate by remember { mutableStateOf(now.time) }
    var showDatePicker by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selected = Calendar.getInstance()
                selected.set(year, month, dayOfMonth)
                if (selected.time.after(now.time)) {
                    dueDate = selected.time
                } else {
                    Toast.makeText(context, "Ngày hết hạn phải sau ngày hiện tại", Toast.LENGTH_SHORT).show()
                }
                showDatePicker = false
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Work Order mới") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Tiêu đề") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Mô tả") })
                OutlinedTextField(value = assignedTo, onValueChange = { assignedTo = it }, label = { Text("Giao cho") })

                Spacer(modifier = Modifier.height(8.dp))

                Text("Ngày hết hạn: ${SimpleDateFormat("dd/MM/yyyy").format(dueDate)}")
                Button(onClick = { showDatePicker = true }) {
                    Text("Chọn ngày hết hạn")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Ảnh đã chọn
                imageUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }

                Button(onClick = {
                    launcher.launch("image/*")
                }) {
                    Text("Chọn ảnh")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    uploading = true
                    val workOrderId = UUID.randomUUID().toString()

                    if (imageUri != null) {
                        // Tạo reference Firebase Storage
                        val ref = storage.reference.child("workorders/${workOrderId}.jpg")
                        ref.putFile(imageUri!!)
                            .continueWithTask { task ->
                                if (!task.isSuccessful) throw task.exception!!
                                ref.downloadUrl
                            }
                            .addOnSuccessListener { uri ->
                                val workOrder = WorkOrder(
                                    id = workOrderId,
                                    title = title,
                                    description = description,
                                    assigned_to = assignedTo,
                                    status = "Pending",
                                    created_at = Timestamp(now.time),
                                    due_date = Timestamp(dueDate),
                                    imageUrl = uri.toString()
                                )
                                uploading = false
                                onAdd(workOrder)
                            }
                            .addOnFailureListener {
                                uploading = false
                                Toast.makeText(context, "Lỗi khi upload ảnh", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // Không có ảnh, tạo work order bình thường
                        val workOrder = WorkOrder(
                            id = workOrderId,
                            title = title,
                            description = description,
                            assigned_to = assignedTo,
                            status = "Pending",
                            created_at = Timestamp(now.time),
                            due_date = Timestamp(dueDate),
                            imageUrl = null
                        )
                        uploading = false
                        onAdd(workOrder)
                    }
                },
                enabled = !uploading
            ) {
                Text(if (uploading) "Đang lưu..." else "Lưu")
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
        "completed" -> Color(0xFF4CAF50)
        "in_progress" -> Color(0xFFB28DFF)
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


