package com.example.kotlinapp.user

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kotlinapp.R
import com.example.kotlinapp.user.UserViewModel

@Composable
fun ProfileScreen(userId: String,viewModel: UserViewModel = viewModel()) {
    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Tạo đối tượng UserRepository
    val userRepository = UserViewModel()

    // Gọi getUserById trong LaunchedEffect
    LaunchedEffect(userId) {
        userRepository.getUserById(userId) { fetchedUser ->
            user = fetchedUser
            isLoading = false
        }
    }

    // Hiển thị khi đang tải dữ liệu
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        user?.let { userData ->
            // Hiển thị thông tin người dùng khi có dữ liệu
            UserProfileContent(userData)
        } ?: run {
            // Hiển thị thông báo nếu không tìm thấy người dùng
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Không tìm thấy người dùng")
            }
        }
    }
}



@Composable
fun UserProfileContent(user: User) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.uth_logo),
            error = painterResource(id = R.drawable.uth_logo)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Tên: ${user.name}", style = MaterialTheme.typography.titleMedium)
        Text("Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
        Text("SĐT: ${user.phone}", style = MaterialTheme.typography.bodyMedium)
        Text("Vai trò: ${user.role}", style = MaterialTheme.typography.bodyMedium)
    }
}

