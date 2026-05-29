package com.example.lyceum_saturday10_2025.features.todo.AuthScreen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lyceum_saturday10_2025.features.destinations.LoginScreenDestination
import com.example.lyceum_saturday10_2025.features.destinations.RegisterScreenDestination
import com.example.lyceum_saturday10_2025.features.destinations.TodoScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Composable
@Destination
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()
    var validationError by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState is AuthViewModel.AuthState.Success) {
            navigator.navigate(TodoScreenDestination) {
                popUpTo(LoginScreenDestination) { inclusive = true }
            }
        }
    }

    if (viewModel.isLoggedIn()) {
        LaunchedEffect(Unit) {
            navigator.navigate(TodoScreenDestination) {
                popUpTo(LoginScreenDestination) { inclusive = true }
            }
        }
        return
    }

    LaunchedEffect(Unit) {
        Log.d("NAV", "LoginScreen opened, isLoggedIn=${viewModel.isLoggedIn()}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = {
                if (!it.endsWith('\n') and !it.endsWith(' ')) {
                    username = it
                }
            },
            label = { Text("Имя пользователя") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                if (!it.endsWith('\n') and !it.endsWith(' ')) {
                    password = it
                }
            },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (authState is AuthViewModel.AuthState.Error) {
            Text(
                text = (authState as AuthViewModel.AuthState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (validationError.isNotEmpty()) {
            Text(
                text = validationError,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (authState is AuthViewModel.AuthState.ServerUnavailable) {
            Text(
                text = "Сервер недоступен",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (username.isBlank() || password.isBlank()) {
                    validationError = "Заполните все поля"
                } else {
                    validationError = ""
                    viewModel.login(username, password)
                }
            },
            enabled = authState !is AuthViewModel.AuthState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navigator.navigate(RegisterScreenDestination) {
                    popUpTo(LoginScreenDestination) { inclusive = true }
                }
            }
        ) {
            Text("Нет аккаунта? Регистрация")
        }
    }
}


@Composable
@Preview(showBackground = true)
private fun LoginScreenPreview() {
    LoginScreen(
        navigator = EmptyDestinationsNavigator
    )
}