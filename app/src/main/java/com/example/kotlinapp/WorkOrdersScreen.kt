package com.example.kotlinapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.viewmodel.WorkOrdersViewModel
import com.example.kotlinapp.model.WorkOrder

@Composable
fun WorkOrdersScreen(viewModel: WorkOrdersViewModel = viewModel()) {
    val workOrders by viewModel.workOrders.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(workOrders) { order ->
            WorkOrderItem(order)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun WorkOrderItem(order: WorkOrder) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(order.title, style = MaterialTheme.typography.titleMedium)
            Text(order.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
