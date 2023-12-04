package com.example.bemyplant.data

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.bemyplant.MJ_MainActivity
import com.example.bemyplant.network.RetrofitService
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val retrofitService = RetrofitService().apiService

@RequiresApi(Build.VERSION_CODES.O)
fun checkIfSensorDataIsLatest(context: Context, callback: (Boolean) -> Unit) {
    // get token
    val sharedPreferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("token", null)

    runBlocking {
        try {
            // api call to get sensor data
            val response = retrofitService.getSensorData(1, "Bearer $token")

            if (response.code() == 401) {
                // 401 Unauthorized 오류 처리
                // 첫 화면으로 (로그인 시도 화면) 이동
                val homeIntent = Intent(context, MJ_MainActivity::class.java)
                context.startActivity(homeIntent)
                callback.invoke(false)
                return@runBlocking
            }

            if (response.isSuccessful) {
                val sensorDataList = response.body()
                if (!sensorDataList.isNullOrEmpty()) {
                    val mostRecentData = sensorDataList.firstOrNull()
                    mostRecentData?.date?.let {
                        Log.d("sensor is latest", "recent data: $it")
                        // Check if it's the most recent time
                        val isLatest = checkTimeDifference(it)
                        callback.invoke(isLatest)
                    }
                }
            }
            else{
                callback.invoke(false)
            }
        } catch (e: Exception) {
            // Handle exceptions (e.g., network issues)
            Log.d("sensor is latest", "Error during API call")
            callback.invoke(false)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun checkTimeDifference(dataDate: String): Boolean {
    // Parse the date from the JSON data
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
    val dataDateTime = ZonedDateTime.parse(dataDate, formatter)

    // Get the current date and time
    val currentDateTime = ZonedDateTime.now()

    // Calculate the time difference in hours
    val timeDifference = kotlin.math.abs(Duration.between(dataDateTime, currentDateTime).toHours())

    // 1시간 이상 차이나면 false
    return timeDifference < 1
}
