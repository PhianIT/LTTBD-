package com.example.kotlinapp.ui.screen.login

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinapp.R
import com.example.kotlinapp.ui.theme.AppBlue
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.example.kotlinapp.util.SharedPreferencesHelper


@Composable
fun LoginScreen(
    googleSignInClient: GoogleSignInClient,
    firebaseAuth: FirebaseAuth,
    onLoginSuccess: (email: String, name: String) -> Unit,
    onSignUpClick: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val viewModel: LoginViewModel = viewModel()
    val loginState by viewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isGoogleLoading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            isGoogleLoading = false
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val user = firebaseAuth.currentUser
                                val email = user?.email ?: ""
                                val name = user?.displayName ?: "User"
//                                SharedPreferencesHelper.saveUser(context, email, name) // Lưu vào SharedPreferences
                                onLoginSuccess(email, name)
                            } else {
                                Toast.makeText(context, "Google login failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            } catch (e: ApiException) {
                Toast.makeText(context, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginUiState.Success -> {
                val user = firebaseAuth.currentUser
                val email = user?.email ?: ""
                val name = user?.displayName ?: "User"
                SharedPreferencesHelper.saveUser(context, email, name) // Lưu vào SharedPreferences
                Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                onLoginSuccess(email, name)
                viewModel.resetState()
            }
            is LoginUiState.Error -> {
                Toast.makeText(context, (loginState as LoginUiState.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_ai),
                    contentDescription = "Logo Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Đăng nhập", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = AppBlue)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Vui lòng đăng nhập vào tài khoản của bạn", color = Color.Gray)

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { newValue ->
                            email = newValue
                                .filter { it.isLetterOrDigit() || it == '@' || it == '.' } // Chỉ cho chữ, số, @ và .
                                .replace(" ", "") // Bỏ khoảng trắng
                        },
                        placeholder = { Text("Tài khoản") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_user),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { newValue ->
                            password = newValue
                                .filter { it.isLetterOrDigit() } // Chỉ cho chữ và số
                                .replace(" ", "") // Bỏ khoảng trắng
                        },
                        placeholder = { Text("Mật khẩu") },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lock),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Gray
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    painter = painterResource(
                                        id = if (isPasswordVisible) R.drawable.ic_eye else R.drawable.ic_eye_off
                                    ),
                                    contentDescription = "Toggle Password",
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Gray
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Quên mật khẩu?", color = AppBlue, fontSize = 14.sp, modifier = Modifier.align(Alignment.End))
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            when {
                                email.isBlank() -> {
                                    Toast.makeText(context, "Bạn chưa nhập tài khoản!", Toast.LENGTH_SHORT).show()
                                }
                                password.isBlank() -> {
                                    Toast.makeText(context, "Bạn chưa nhập mật khẩu!", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    viewModel.loginWithEmail(email.trim(), password)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppBlue)
                    ) {
                        Text("Đăng nhập", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            TextButton(
                                onClick = { onSignUpClick() },
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(text = "Bạn chưa có tài khoản?", color = AppBlue, fontSize = 14.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = {
                                isGoogleLoading = true

                                // Đăng xuất khỏi Firebase và Google để đăng nhập tài khoản khác
                                firebaseAuth.signOut()
                                googleSignInClient.signOut().addOnCompleteListener {
                                    val signInIntent = googleSignInClient.signInIntent
                                    launcher.launch(signInIntent)
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_google),
                                contentDescription = "Google",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Google")
                        }

                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_facebook),
                                contentDescription = "Facebook",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Facebook")
                        }
                    }
                }
            }
        }
    }
}
