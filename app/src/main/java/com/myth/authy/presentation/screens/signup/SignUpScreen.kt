package com.myth.authy.presentation.screens.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.myth.authy.core.utils.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpPage(
    navController: NavController, viewModel: SignUpViewModel = hiltViewModel()
) {
    var signingUp by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var resetPassEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var repeatPassword by remember { mutableStateOf("") }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.padding(16.dp),
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!signingUp) {
                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = "Welcome Back!",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = "Sign in to continue",
                )
            } else {
                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = "Hi! \uD83D\uDC4B",
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = "Create an account to continue",
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, contentDescription = description)
                    }
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            )
            if (!signingUp) {
                Spacer(modifier = Modifier.height(8.dp))
                ClickableText(
                    modifier = Modifier.align(Alignment.End),
                    text = AnnotatedString(text = "Forgot password?"),
                    onClick = { showBottomSheet = true })
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (signingUp) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = repeatPassword,
                    onValueChange = { repeatPassword = it },
                    label = { Text("Repeat Password") },
                    trailingIcon = {
                        val image =
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff

                        val description = if (passwordVisible) "Hide password" else "Show password"
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, contentDescription = description)
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (signingUp) {
                Button(
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        viewModel.registerWithEmailAndPassword(
                            email, password, repeatPassword
                        ) { success, error ->
                            if (success) {
                                scope.launch {
                                    snackBarHostState.showSnackbar("Sign up successful")
                                }
                                navController.navigate(Screen.Home.route)
                            } else {
                                scope.launch {
                                    snackBarHostState.showSnackbar("$error")
                                }
                            }
                        }
                    }) {
                    Text("Sign up")
                }
                ClickableText(text = AnnotatedString(text = "Already have an account? Sign in"),
                    onClick = { signingUp = false })
            } else {
                Button(
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        viewModel.loginWithEmail(email, password) { success, error ->
                            if (success) {
                                scope.launch {
                                    snackBarHostState.showSnackbar("Sign in Successful")
                                }
                                navController.navigate(Screen.Home.route)
                            } else {
                                scope.launch {
                                    snackBarHostState.showSnackbar("$error")
                                }
                            }
                        }
                    }) {
                    Text("Sign in")
                }
                ClickableText(text = AnnotatedString(text = "Don't have an account? Sign up"),
                    onClick = { signingUp = true })
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Send reset link to")
                        Spacer(Modifier.height(4.dp))
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = resetPassEmail,
                            onValueChange = { resetPassEmail = it },
                            placeholder = { Text("Email") },
                            singleLine = true,
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            shape = RoundedCornerShape(8.dp),
                            onClick = {
                                showBottomSheet = false
                                viewModel.resetAccountPassword(resetPassEmail) { success, error ->
                                    if (success) {
                                        scope.launch {
                                            snackBarHostState.showSnackbar("Reset link sent to email")
                                        }
                                    } else {
                                        scope.launch {
                                            snackBarHostState.showSnackbar("$error")
                                        }
                                    }
                                }
                            }) {
                            Text("Send reset link")
                        }
                    }
                }
            }
        }
    }
}

