package com.example.animalscopecasptone.repository

import com.example.animalscopecasptone.response.login.LoginRequest
import com.example.animalscopecasptone.response.login.LoginResponse
import com.example.animalscopecasptone.response.register.RegisterRequest
import com.example.animalscopecasptone.response.register.RegisterResponse
import com.example.animalscopecasptone.retorfit.ApiService
import retrofit2.Response

class AuthRepository(
    private val apiService: ApiService,
) {
    suspend fun registerUser(registerRequestBody: RegisterRequest) : Response<RegisterResponse> {
        return apiService.registerUser(registerRequestBody)
    }

    suspend fun loginUser(loginUserRequestBody: LoginRequest) : Response<LoginResponse> {
        return apiService.loginUser(loginUserRequestBody)
    }

}