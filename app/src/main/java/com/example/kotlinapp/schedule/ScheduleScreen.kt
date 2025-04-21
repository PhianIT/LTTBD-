package com.example.kotlinapp.schedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.workorder.WorkOrdersViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleScreen(viewModel: WorkOrdersViewModel = viewModel()) {
    val workOrders by viewModel.workOrders.collectAsState()
    val currentDate = LocalDate.now()
    val yearMonth = YearMonth.of(currentDate.year, currentDate.month)
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Sunday = 0, Monday = 1, ...
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    // Convert work orders' due dates to LocalDate for comparison
    val workOrderDates = workOrders
        .filter { it.due_date != null }
        .mapNotNull { it.due_date?.toDate()?.toLocalDate() }
        .filter { it.year == currentDate.year && it.month == currentDate.month }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Month and Year Header
        Text(
            text = "${currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentDate.year}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Days of Week Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Calendar Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            // Empty cells for days before the first day of month
            items(firstDayOfWeek) {
                Box(modifier = Modifier.size(48.dp))
            }

            // Days of the month
            items(yearMonth.lengthOfMonth()) { index ->
                val day = index + 1
                val date = yearMonth.atDay(day)
                val hasWorkOrder = workOrderDates.any { it == date }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            selectedDate = date
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = day.toString(),
                            fontSize = 16.sp,
                            color = if (date == currentDate) MaterialTheme.colorScheme.primary else Color.Black
                        )
                        if (hasWorkOrder) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        selectedDate?.let { date ->
            val ordersOfDay = workOrders.filter {
                it.due_date?.toDate()?.toLocalDate() == date
            }

            Text(
                text = "Công việc ngày ${date.dayOfMonth}/${date.monthValue}/${date.year}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Column {
                ordersOfDay.forEach { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("📌 ${order.title}", fontWeight = FontWeight.Bold)
                            Text("Giao cho: ${order.assigned_to}")
                            Text("Mô tả: ${order.description}")
                        }
                    }
                }
                if (ordersOfDay.isEmpty()) {
                    Text(
                        text = "Không có công việc nào trong ngày này.",
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

    }
}

// Extension function to convert Firebase Timestamp to LocalDate
@RequiresApi(Build.VERSION_CODES.O)
fun Date.toLocalDate(): LocalDate {
    return toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
}
