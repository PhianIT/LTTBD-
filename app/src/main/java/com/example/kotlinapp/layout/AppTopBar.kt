package com.example.kotlinapp.layout
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.kotlinapp.notification.NotificationMenu
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlinapp.screens.WorkOrderItem
import com.example.kotlinapp.ui.screen.assets.ThemeViewModel
import com.example.kotlinapp.workorder.WorkOrder
import com.example.kotlinapp.workorder.WorkOrdersViewModel
import com.google.firebase.components.Lazy
import com.example.kotlinapp.ui.screen.assets.Asset
import com.example.kotlinapp.ui.screen.assets.AssetCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSearch: (String) -> Unit, // Callback khi tìm kiếm
    navController: NavController
) {
    Log.d("SEARCH",title);
    var data by remember { mutableStateOf(emptyList<WorkOrder>()) }
    var dataAsset by remember { mutableStateOf(emptyList<Asset>()) }
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val ordersViewModel: WorkOrdersViewModel = viewModel()
    val workOrders by ordersViewModel.workOrders.collectAsState()

    val themeViewModel: ThemeViewModel = viewModel()
    val assetList by themeViewModel.assetList.collectAsState()


    if (active) {
        // Khi SearchBar được kích hoạt
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it;
            // tìm với lệnh làm việc
                data = workOrders.filter { it.title.contains(searchQuery, ignoreCase = true);}
                    // tìm với tài sản
                dataAsset = assetList.filter { it.name.contains(searchQuery, ignoreCase = true)            } },
            onSearch = {
                onSearch(it)
                data = workOrders.filter { it.title.contains(it.toString(), ignoreCase = true) }
                active = false
            },
            active = active,
            onActiveChange = { active = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Tìm kiếm...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Tìm kiếm")
            },
            trailingIcon = {
                IconButton(onClick = {
                    active = false
                    searchQuery = ""
                }) {
                    Icon(Icons.Default.Close, contentDescription = "Đóng")
                }
            }
        ) {
            // Danh sách kết quả tìm kiếm nếu cần hiển thị
            Text("Kết quả cho: $searchQuery", modifier = Modifier.padding(16.dp))
            Log.d("DATA",data.toString());

            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(data) { order ->
                    // Truyền navController vào cho WorkOrderItem
                    WorkOrderItem(order = order, navController)
                }
            }

            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(dataAsset) { asset ->
                    AssetCard(asset = asset)
                }
            }

        }
    } else {
        // TopAppBar mặc định
        TopAppBar(
            title = { Text(title) },
            actions = {
                IconButton(onClick = { active = true }) {
                    Icon(Icons.Default.Search, contentDescription = "Tìm kiếm")
                }
                IconButton(onClick = onNotificationClick) {
                    NotificationMenu() // Hoặc Icon nếu bạn chưa định nghĩa NotificationMenu
                }
                IconButton(onClick = onProfileClick) {
                    Icon(Icons.Default.Face, contentDescription = "Hồ sơ cá nhân")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF1E88E5),
                titleContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )
    }
}


