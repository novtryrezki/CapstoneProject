package com.example.animalscopecasptone.response.login

import com.example.animalscopecasptone.response.Errors
import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("email")
    val email: String
)

data class LoginResponse(
    val errors: Errors,
    val message: String,
    val token: String
)