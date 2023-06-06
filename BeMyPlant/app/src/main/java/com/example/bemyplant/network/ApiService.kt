package com.example.bemyplant.network
import com.example.bemyplant.data.LoginData
import com.example.bemyplant.data.SignUpData
import com.example.bemyplant.data.LoginResponse
import com.example.bemyplant.data.SignUpResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body

interface ApiService {
    @POST("api/authenticate")
    suspend fun login(@Body loginData: LoginData): Response<LoginResponse>
    /*suspend : 비동기 호출 -> 코루틴 사용
    *fun login(@Body loginData: LoginData): Response<LoginResponse>:
    * loginData라는 이름의 LoginData 객체를 요청의 바디에 담아 서버로 보내고, Response<LoginResponse> 객체를 반환.*/

    @POST("api/signup")
    suspend fun signUp(@Body signUpData: SignUpData): Response<SignUpResponse>

}

