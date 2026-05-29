package com.example.lyceum_saturday10_2025.features.todo.MainScreen.presentation.model

data class TodoUiState(
    val items: List<TodoItemUi> = emptyList(),
    val isUnauthorized: Boolean = false,
    val showServerError: Boolean = false
)