package com.example.bemyplant.data
import java.util.Date

data class LoginData(
    val username: String,
    val password: String
)
data class SignUpData(
    val username: String,
    val password: String,
    val phones: String,
    val r_name: String,
    val date: Date,
    val activated: Int = 1
)
data class LoginResponse(
    val status: String,
    val message: String,
    val token: String
)
data class SignUpResponse(
    val status: String,
    val message: String,
    val userId: String
)