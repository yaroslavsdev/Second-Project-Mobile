package com.example.lyceum_saturday10_2025.features.todo.AuthScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lyceum_saturday10_2025.features.destinations.LoginScreenDestination
import com.example.lyceum_saturday10_2025.features.destinations.TodoScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
fun RegisterScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(Unit) {
        if (viewModel.isLoggedIn()) {
            navigator.navigate(TodoScreenDestination)
        }
    }

    LaunchedEffect(authState) {
        if (authState is AuthViewModel.AuthState.Success) {
            navigator.navigate(TodoScreenDestination)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Имя пользователя") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
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

        if (authState is AuthViewModel.AuthState.ServerUnavailable) {
            Text(
                text = "Сервер недоступен",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.register(username, password) },
            enabled = authState !is AuthViewModel.AuthState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрироваться")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navigator.navigate(LoginScreenDestination) }
        ) {
            Text("Есть аккаунт? Вход")
        }
    }
}