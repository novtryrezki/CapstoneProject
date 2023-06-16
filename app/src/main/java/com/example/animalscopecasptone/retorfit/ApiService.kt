package com.example.animalscopecasptone.retorfit

import com.example.animalscopecasptone.response.login.LoginRequest
import com.example.animalscopecasptone.response.login.LoginResponse
import com.example.animalscopecasptone.response.register.RegisterRequest
import com.example.animalscopecasptone.response.register.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Headers("Accept: application/json")
    @POST("users")
    suspend fun registerUser(
        @Body body: RegisterRequest
    ): Response<RegisterResponse>

    @Headers("Accept: application/json")
    @POST("logins")
    suspend fun loginUser(
        @Body body: LoginRequest
    ): Response<LoginResponse>


}