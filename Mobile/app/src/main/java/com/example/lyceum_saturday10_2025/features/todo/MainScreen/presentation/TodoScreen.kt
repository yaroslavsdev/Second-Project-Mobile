package com.example.lyceum_saturday10_2025.features.todo.MainScreen.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lyceum_saturday10_2025.features.destinations.LoginScreenDestination
import com.example.lyceum_saturday10_2025.features.destinations.TodoScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun TodoScreen(
    navigator: DestinationsNavigator
) {
    val viewmodel = viewModel<TodoViewModel>()
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(state.isUnauthorized) {
        if (state.isUnauthorized) {
            viewmodel.clearUnauthorized()
            navigator.navigate(LoginScreenDestination) {
                launchSingleTop = true
            }
        }
    }

    LaunchedEffect(Unit) {
        Log.d("NAV", "TodoScreen opened, isUnauthorized=${state.isUnauthorized}")
    }

    TodoScreenContent(state) { text ->
        viewmodel.addItem(text)
    }
}