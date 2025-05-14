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
import com.example.kotlinapp.ui.screen.inventory.Inventory
import com.example.kotlinapp.ui.screen.inventory.InventoryCard
import com.example.kotlinapp.ui.screen.inventory.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSearch: (String) -> Unit, // Callback khi tìm kiếm
    navController: NavController,
    currentScreen: String
) {
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    var dataOrders by remember { mutableStateOf(emptyList<WorkOrder>()) }
    var dataAsset by remember { mutableStateOf(emptyList<Asset>()) }
    var dataInventory by remember { mutableStateOf(emptyList<Inventory>()) }

    val ordersViewModel: WorkOrdersViewModel = viewModel()
    val workOrders by ordersViewModel.workOrders.collectAsState()

    val themeViewModel: ThemeViewModel = viewModel()
    val assetList by themeViewModel.assetList.collectAsState()

    val viewModel: InventoryViewModel = viewModel()
    val inventoryList by viewModel.inventoryList.collectAsState()

    if (active) {
        // Khi SearchBar được kích hoạt
        SearchBar(
            query = searchQuery,
            onQueryChange = {
                searchQuery = it

                // Lọc dữ liệu dựa trên màn hình hiện tại
                when (title) {
                    "Lệnh làm việc" -> dataOrders = workOrders.filter { order ->
                        order.title.contains(searchQuery, ignoreCase = true)
                    }
                    "Tài sản" -> dataAsset = assetList.filter { asset ->
                        asset.name.contains(searchQuery, ignoreCase = true)
                    }
                    "Hạng mục kho" -> dataInventory = inventoryList.filter { inventory ->
                        inventory.name.contains(searchQuery, ignoreCase = true)
                    }
                    else -> {} // Không làm gì cho các màn hình khác
                }
            },
            onSearch = {
                onSearch(it)
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
            // Danh sách kết quả tìm kiếm
            Text("Kết quả cho: $searchQuery", modifier = Modifier.padding(16.dp))

            when (title) {
                "Lệnh làm việc" -> LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(dataOrders) { order ->
                        WorkOrderItem(order = order, navController)
                    }
                }
                "Tài sản" -> LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(dataAsset) { asset ->
                        AssetCard(asset = asset)
                    }
                }
                "Hạng mục kho" -> LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(dataInventory) { inventory ->
                        InventoryCard(inventory = inventory)
                    }
                }
                else -> Text("Không có kết quả tìm kiếm")
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
                    NotificationMenu()
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


