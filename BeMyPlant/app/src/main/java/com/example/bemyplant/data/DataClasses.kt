package com.example.bemyplant.data
import java.util.Date

data class LoginData(
    val username: String,
    val password: String
)
data class LoginResponse(
    /*val status: String,
    val message: String,*/
    val token: String
)
data class SignUpData(
    val username: String,
    val password: String,
    val phones: String,
    val r_name: String,
    val cre_date: String,
    val activated: Int = 1
)
data class SignUpResponse(
    val username: String,
    val r_name: String,
    val phones: String,
    val cre_date: String,
    val authorityDtoSet:  List<AuthorityDto>
)

data class AuthorityDto(
    val authorityName: String
)
