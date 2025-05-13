package com.example.kotlinapp.layout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSearch: (String) -> Unit // Callback khi tìm kiếm
) {
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    if (active) {
        // Khi SearchBar được kích hoạt
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
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
            // Danh sách kết quả tìm kiếm nếu cần hiển thị
            Text("Kết quả cho: $searchQuery", modifier = Modifier.padding(16.dp))

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


