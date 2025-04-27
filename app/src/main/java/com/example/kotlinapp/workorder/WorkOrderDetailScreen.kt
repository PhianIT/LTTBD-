import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.kotlinapp.workorder.WorkOrder
import com.example.kotlinapp.workorder.WorkOrdersViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.kotlinapp.R

@Composable
fun WorkOrderDetailScreen(orderId: String?) {
    val viewModel: WorkOrdersViewModel = viewModel()
    val workOrder = remember { mutableStateOf<WorkOrder?>(null) }

    LaunchedEffect(orderId) {
        if (orderId != null) {
            viewModel.fetchWorkOrderById(orderId) { order ->
                workOrder.value = order
            }
        }
    }

    workOrder.value?.let { order ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Chi tiết Work Order",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Load ảnh từ URL bằng Coil
                Card(
                    modifier = Modifier
                        .size(120.dp)
                        .padding(end = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(R.drawable.uth_logo) // URL ảnh từ Firebase
                            .crossfade(true)
                            .build(),
                        contentDescription = "Ảnh công việc",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    DetailText(label = "Tiêu đề", value = order.title)
                    DetailText(label = "Trạng thái", value = order.status)
                    DetailText(label = "Giao cho", value = order.assigned_to)
                }
            }

            DetailCard(label = "Mô tả", value = order.description)
            DetailCard(label = "Ngày tạo", value = order.created_at?.toDate().toString())
            DetailCard(label = "Hạn chót", value = order.due_date?.toDate().toString())
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun DetailText(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
@Composable
fun DetailCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // bỏ bóng đổ
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
        }
    }
}


