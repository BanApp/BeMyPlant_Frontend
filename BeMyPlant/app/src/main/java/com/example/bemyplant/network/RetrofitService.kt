package com.example.bemyplant.network
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class RetrofitService {
        private val retrofit = Retrofit.Builder()
            .baseUrl("http://141.164.35.145:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService: ApiService = retrofit.create(ApiService::class.java)


        private val retrofit2 = Retrofit.Builder()
            .baseUrl("http://141.164.35.145:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService2: ApiService = retrofit2.create(ApiService::class.java)
}