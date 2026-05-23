package com.example.lyceum_saturday10_2025.common.api

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/login")
    suspend fun login(@Body body: AuthRequest): TokensResponse

    @POST("/register")
    suspend fun register(@Body body: AuthRequest): TokensResponse
}

data class AuthRequest(
    val username: String,
    val password: String
)