//package com.example.lyceum_saturday10_2025.features.todo.AuthScreen
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.example.lyceum_saturday10_2025.common.UserPrefsManager
//import kotlinx.coroutines.launch
//
//class AuthViewModel(application: Application) : AndroidViewModel(application) {
//    private val prefs = UserPrefsManager(application)
//
//    private val authApi = RetrofitClient.authApi
//
//    sealed class AuthState {
//        object Success : AuthState()
//        data class Error(val message: String) : AuthState()
//        object Loading : AuthState()
//    }
//
//    val authState = MutableLiveData<AuthState>()
//
//    fun login(username: String, password: String) = viewModelScope.launch {
//        authState.value = AuthState.Loading
//        try {
//            val response = authApi.login(AuthRequest(username, password))
//            prefs.accessToken = response.access_token
//            prefs.refreshToken = response.refresh_token
//            prefs.username = username
//            authState.value = AuthState.Success
//        } catch (e: Exception) {
//            authState.value = AuthState.Error("Неверный логин или пароль")
//        }
//    }
//
//    fun register(username: String, password: String) = viewModelScope.launch {
//        authState.value = AuthState.Loading
//        try {
//            val response = authApi.register(AuthRequest(username, password))
//            prefs.accessToken = response.access_token
//            prefs.refreshToken = response.refresh_token
//            prefs.username = username
//            authState.value = AuthState.Success
//        } catch (e: Exception) {
//            authState.value = AuthState.Error("Пользователь уже существует")
//        }
//    }
//
//    fun isLoggedIn() = prefs.accessToken != null
//}