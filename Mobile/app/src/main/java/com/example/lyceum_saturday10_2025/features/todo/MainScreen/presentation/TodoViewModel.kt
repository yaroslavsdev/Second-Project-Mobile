package com.example.lyceum_saturday10_2025.features.todo.MainScreen.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lyceum_saturday10_2025.features.todo.MainScreen.data.TodoRepository
import com.example.lyceum_saturday10_2025.features.todo.MainScreen.presentation.model.TodoItemUi
import com.example.lyceum_saturday10_2025.features.todo.MainScreen.presentation.model.TodoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    val repository = TodoRepository(application.applicationContext)
    private val _state = MutableStateFlow(TodoUiState())
    val state: StateFlow<TodoUiState>
        get() = _state

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            try {
                _state.emit(
                    TodoUiState(
                        repository.getItems().map { item ->
                            TodoItemUi(id = item.id, text = item.text)
                        }
                    )
                )
            } catch (e: Exception) {
                repository.prefs.clearUser()
                _state.emit(TodoUiState(isUnauthorized = true))
            }
        }
    }

    fun addItem(text: String) {
        viewModelScope.launch {
            try {
                repository.addItem(text)
                loadItems()
            } catch (e: Exception) {
                repository.prefs.clearUser()
                _state.emit(TodoUiState(isUnauthorized = true))
            }
        }
    }

    fun clearUnauthorized() {
        _state.value = _state.value.copy(isUnauthorized = false)
    }
}