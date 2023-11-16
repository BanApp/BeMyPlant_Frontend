package com.example.bemyplant.network
import com.example.bemyplant.data.ChatRequest
import com.example.bemyplant.data.ChatResponse
import com.example.bemyplant.data.GardenResponse
import com.example.bemyplant.data.LoginData
import com.example.bemyplant.data.SignUpData
import com.example.bemyplant.data.SensorData
import com.example.bemyplant.data.LoginResponse
import com.example.bemyplant.data.SignUpResponse
import com.example.bemyplant.data.UserData
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @POST("api/authenticate")
    suspend fun login(@Body loginData: LoginData): Response<LoginResponse>
    /*suspend : 비동기 호출 -> 코루틴 사용
    *fun login(@Body loginData: LoginData): Response<LoginResponse>:
    * loginData라는 이름의 LoginData 객체를 요청의 바디에 담아 서버로 보내고, Response<LoginResponse> 객체를 반환.*/

    @POST("api/signup")
    suspend fun signUp(@Body signUpData: SignUpData): Response<SignUpResponse>

    @POST("api/chatbot")
    suspend fun chat(@Body chatRequest: ChatRequest, @Header("Authorization") token: String?): Response<ChatResponse>

    @GET("api/get-sensor-data")
    suspend fun getSensorData(@Query("num") num: Int, @Header("Authorization") token: String?): Response<ArrayList<SensorData>>


    @GET("api/user")
    suspend fun getUserData(@Header("Authorization") token: String?): Response<UserData>

    @DELETE("api/withdrawal")
    suspend fun deleteAccount(@Header("Authorization") token: String?): Response<ResponseBody>

    @GET("garden/gardenDtl")
    //suspend fun getSensorData(@Query("num") num: Int): Response<SensorsData>
    suspend fun getGardenDetail(@Query("apiKey") apiKey: String,@Query("cntntsNo") cntntsNo: String): Response<GardenResponse>



}

