package com.example.animalscopecasptone.response.register

import com.example.animalscopecasptone.response.Errors
import com.google.gson.annotations.SerializedName

data class RegisterRequest(

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("userName")
    val userName: String,

    @field:SerializedName("email")
    val email: String
)

data class RegisterResponse(
    val errors: Errors,
    val message: String
)