package com.example.kotlinapp.ui.screen.signup

import android.app.Activity
import android.util.Patterns
import android.widget.Toast
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
import androidx.navigation.NavHostController
import com.example.kotlinapp.R
import com.example.kotlinapp.ui.theme.AppBlue
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun SignUpScreen(
    googleSignInClient: GoogleSignInClient,
    navController: NavHostController,
    firebaseAuth: FirebaseAuth,
    onLoginSuccess: (String, String) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
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
                                // ✅ Lưu thông tin user
                                com.example.kotlinapp.util.SharedPreferencesHelper.saveUser(context, email, name)
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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize())       {
            // Header Image
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
                        .padding(24.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Đăng kí", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = AppBlue)
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

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { newValue ->
                            confirmPassword = newValue
                                .filter { it.isLetterOrDigit() } // Chỉ cho chữ và số
                                .replace(" ", "") // Bỏ khoảng trắng
                        },
                        placeholder = { Text("Nhập lại mật khẩu") },
                        visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lock),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Gray
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                                Icon(
                                    painter = painterResource(
                                        id = if (isConfirmPasswordVisible) R.drawable.ic_eye else R.drawable.ic_eye_off
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                Toast.makeText(context, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            if (password.length < 6) {
                                Toast.makeText(context, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            if (password != confirmPassword) {
                                Toast.makeText(context, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isLoading = true
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Đăng kí thành công", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack() // Quay lại Login
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Đăng kí thất bại: ${task.exception?.localizedMessage}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        Log.e("SignUpScreen", "SignUp failed", task.exception)
                                    }
                                }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppBlue)
                    ) {
                        Text("Đăng kí", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = {
                        navController.navigate("login")
                    }) {
                        Text("Bạn đã có tài khoản?", color = AppBlue)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Divider()

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        OutlinedButton(
                            onClick = {
                                isGoogleLoading = true
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

                        OutlinedButton(onClick = { /* TODO: Handle Facebook SignUp */ }) {
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