package com.example.bemyplant.network
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class RetrofitService {
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://61.77.7.188:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService: ApiService = retrofit.create(ApiService::class.java)
}