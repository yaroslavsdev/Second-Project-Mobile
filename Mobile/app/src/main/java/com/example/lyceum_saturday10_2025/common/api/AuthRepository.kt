package com.example.lyceum_saturday10_2025.common.api

import android.content.Context
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepository(val applicationContext: Context) {

    private val api: AuthApi by lazy {
        val httpClient = Builder()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient.addInterceptor(logging)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        retrofit.create(AuthApi::class.java)
    }

    suspend fun login(username: String, password: String): TokensResponse {
        return api.login(AuthRequest(username, password))
    }

    suspend fun register(username: String, password: String): TokensResponse {
        return api.register(AuthRequest(username, password))
    }
}