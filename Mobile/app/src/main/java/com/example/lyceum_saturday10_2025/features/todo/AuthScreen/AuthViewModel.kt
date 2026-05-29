package com.example.lyceum_saturday10_2025.features.todo.AuthScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lyceum_saturday10_2025.common.UserPrefsManager
import com.example.lyceum_saturday10_2025.common.api.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = UserPrefsManager(application)

    private val repository = AuthRepository(application)

    sealed class AuthState {
        object Idle : AuthState()
        object Success : AuthState()
        data class Error(val message: String) : AuthState()
        object Loading : AuthState()
        object ServerUnavailable : AuthState()
    }

    val authState = MutableStateFlow<AuthState>(AuthState.Idle)

    fun login(username: String, password: String) = viewModelScope.launch {
        authState.value = AuthState.Loading
        try {
            val response = repository.login(username, password)
            prefs.accessToken = response.access_token
            prefs.refreshToken = response.refresh_token
            prefs.username = username
            authState.value = AuthState.Success
        } catch (e: java.io.IOException) {
            authState.value = AuthState.ServerUnavailable
        } catch (e: Exception) {
            authState.value = AuthState.Error("Неверный логин или пароль")
        }
    }

    fun register(username: String, password: String) = viewModelScope.launch {
        authState.value = AuthState.Loading
        try {
            val response = repository.register(username, password)
            prefs.accessToken = response.access_token
            prefs.refreshToken = response.refresh_token
            prefs.username = username
            authState.value = AuthState.Success
        } catch (e: java.io.IOException) {
            authState.value = AuthState.ServerUnavailable
        } catch (e: Exception) {
            authState.value = AuthState.Error("Пользователь уже существует")
        }
    }

    fun isLoggedIn() = prefs.accessToken != null

    fun logout() {
        prefs.clearUser()
        authState.value = AuthState.Idle
    }
}