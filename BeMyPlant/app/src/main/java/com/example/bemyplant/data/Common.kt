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
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.ResolverStyle
import java.util.Locale

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
                        Log.d("checkTimeDifference", isLatest.toString())
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
            //callback.invoke(false)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun checkTimeDifference(dataDate: String): Boolean {
    Log.d("dataDate", dataDate)
    val builder = DateTimeFormatterBuilder()
        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .optionalStart()
        .appendOffset("+HH:MM", "Z")
        .toFormatter(Locale.ENGLISH)
        .withResolverStyle(ResolverStyle.STRICT)

    try {
        val dataDateTime = ZonedDateTime.parse(dataDate, builder)
            .withZoneSameInstant(ZoneId.of("UTC"))

        Log.d("dataDateTime", dataDateTime.toString())

        val currentDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

        Log.d("currentDateTime", currentDateTime.toString())

        val dataTime = dataDateTime.toLocalTime()
        val currentTime = currentDateTime.toLocalTime()

        val timeDifferenceMillis = kotlin.math.abs(Duration.between(dataTime, currentTime).toMillis())
        val timeDifferenceHours = timeDifferenceMillis / (60 * 60 * 1000)

        Log.d("timeDifferenceMillis", timeDifferenceMillis.toString())
        Log.d("timeDifferenceHours", timeDifferenceHours.toString())

        return timeDifferenceHours < 1
    } catch (e: Exception) {
        // Handle parsing exception
        e.printStackTrace()
    }

    return false
}