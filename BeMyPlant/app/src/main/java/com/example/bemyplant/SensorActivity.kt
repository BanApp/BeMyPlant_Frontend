package com.example.bemyplant

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.bemyplant.data.SensorData
import com.example.bemyplant.databinding.ActivitySensorBinding
import com.example.bemyplant.network.ApiService
import com.example.bemyplant.network.RetrofitService
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.collections.ArrayList
import kotlin.collections.firstOrNull
import kotlin.math.abs


class SensorActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySensorBinding
    private val retrofitService = RetrofitService().apiService
    private fun <T> calculateAverage(data: List<SensorData>?, interval: Int, selector: (T) -> Double): Double {
        var sum = 0.0
        var count = 0

        if (data != null) {
            for (i in data.indices step interval) {
                //data[i]와 api 비교 -> 차이값만큼 비교
                var diff = 3 //차이값
                sum += diff
                //sum += data[i]
                count++
            }
        }
        return if (count > 0) sum / count else 0.0
    }

    private fun drawChart(values: List<Int>, labels: List<String>) {
        //그래프 그리기
        val lineChart: LineChart = findViewById(R.id.chart_sensor_sensorChart)
        lineChart.xAxis.valueFormatter= IndexAxisValueFormatter(labels)
        var entries = ArrayList<Entry>()
        for(i in values.indices){
            entries.add(Entry(i.toFloat(), values[i].toFloat()))
        }
        val dataset= LineDataSet(entries, "")
        val data = LineData(dataset)
        lineChart.data = data
        lineChart.invalidate()

    }


    private suspend fun differenceRealVSApi(
        averageAirTemp: Double,
        averageAirHumid: Double,
        averageLightIntensity: Double,
        averageSoilHumid: Double
    ): Array<Double> {
        var diffAirTemp: Double = 0.0
        var diffAirHumid: Double = 0.0
        var diffLightIntensity: Double = 0.0
        var diffSoilHumid: Double = 0.0

        //실내 정원용 식물 api call
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.nongsaro.go.kr/service/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService: ApiService = retrofit.create(ApiService::class.java)

        val response = apiService.getGardenDetail("키값", "조회번호(품종)")
        val sensorDataList = response.body()


        //절댓값 차이

        // 1. 온도 차이
        when (sensorDataList?.grwhTpCode) { //온도 코드 -> 082001(10~15%), 082002(16~20%), 082003(21~25%), 082004(26~30%)
            "082001" -> diffAirTemp = abs(averageLightIntensity - 13.5)
            "082002" -> diffAirTemp = abs(averageLightIntensity - 18)
            "082003" -> diffAirTemp = abs(averageLightIntensity - 23)
            "082004" -> diffAirTemp = abs(averageLightIntensity - 28)
        }


        // 2. 습도 차이
        when (sensorDataList?.hdCode) { //습도 코드 -> 083001(40%미만), 083002(40~70%), 083003(70%이상)
            "083001" -> diffAirHumid = abs(averageLightIntensity - 20)
            "083002" -> diffAirHumid = abs(averageLightIntensity - 55)
            "083003" -> diffAirHumid = abs(averageLightIntensity - 85)
        }


        // 3. 광도 차이
        when (sensorDataList?.lighttdemanddoCode) { //광요구도 코드 -> 055001(550), 055002(1150),055003(5750)
            "05001" -> diffLightIntensity = abs(averageLightIntensity - 550)
            "05002" -> diffLightIntensity = abs(averageLightIntensity - 1150)
            "05003" -> diffLightIntensity = abs(averageLightIntensity - 5750)
        }

        // 4. 토양 습도 차이
        var waterCode: String = "053003"
        // 053001 : 항상 흙을 축축하게 유지함
        // 053002 : 흙을 촉촉하게 유지함(물에 잠기지 않도록 주의)
        // 053003: 토양 표면이 말랐을때 충분히 관수함
        // 053004: 화분 흙 대부분 말랐을때 충분히 관수함

        //계절 판단 -> system의 시간 기반
        val currentDate = Calendar.getInstance()
        when (currentDate.get(Calendar.MONTH) + 1) {
            in 3..5 -> waterCode = sensorDataList?.watercycleSummerCode.toString() //spring
            in 6..8 -> waterCode = sensorDataList?.watercycleSummerCode.toString() //summer
            in 9..11 -> waterCode = sensorDataList?.watercycleSummerCode.toString() //fall
            in 12..2 -> waterCode = sensorDataList?.watercycleSummerCode.toString() //winter
        }
        when (waterCode) {
            "053001" -> diffSoilHumid = abs(averageSoilHumid - 300) //직접 측정에 의해 판단한 값
            "053002" -> diffSoilHumid = abs(averageSoilHumid - 400) //직접 측정에 의해 판단한 값
            "053003" -> diffSoilHumid = abs(averageSoilHumid - 500) //직접 측정에 의해 판단한 값
            "053004" -> diffSoilHumid = abs(averageSoilHumid - 600) //직접 측정에 의해 판단한 값
        }

        return arrayOf(diffAirTemp, diffAirHumid, diffLightIntensity, diffSoilHumid)
    }

    private fun getSensorData() {
        // 현재 시간
        var now = System.currentTimeMillis() / 1000.0 //second
        var day = now % 86400 //하루 86400초 //전날 0시 이후 지난 시간 (초)
        var timeInterval = 100 //1분 40초 = 100초
        var adder: Int = (day / timeInterval).toInt() //몫
        //var num = 48 * 6 + adder //30분 간격 -> 24시간 * 2번 * 6일 + adder
        var num = 6 * 24 * 60 / (timeInterval / 60) + adder

        //var num:Int = 7 * 24 * 60 / (timeInterval/60);


        var format = String.format("%d", day)
        var format2 = String.format("%d", num)
        Log.d("over second", format)
        Log.d("num", format2)

        //api로 가져올 횟수 = num번
        // (1) 현재 날짜에 대해서 call할 횟수 연산 + 지나온 6일
        // (2) 지나온 7일

        CoroutineScope(Dispatchers.IO).launch {

            val response = retrofitService.getSensorData(num)
            if (response.isSuccessful) {
                val intervalMinutes = 30
                val numDataPointsPerHour = 60 / intervalMinutes // 30분 간격으로 추출할 데이터 개수
                val numHours = 24


                val sensorDataList = response.body()
                // 1. date값
                //가장 최근 시간
                if (!sensorDataList.isNullOrEmpty()) {
                    val mostRecentData = sensorDataList.firstOrNull()
                    //button_sensor_soilHumidity

                    val temper: TextView = findViewById(R.id.textView_sensor_temperature)
                    val humid: TextView = findViewById(R.id.textView_button_sensor_humidity)
                    val light: TextView = findViewById(R.id.textView_button_sensor_light)
                    val solidHumid: TextView =
                        findViewById(R.id.textView_button_sensor_soilHumidity)

                    temper.text = mostRecentData?.airTemp.toString()
                    humid.text = mostRecentData?.airHumid.toString()
                    light.text = mostRecentData?.lightIntensity.toString()
                    solidHumid.text = mostRecentData?.soilHumid.toString()

                }

                // 2. 통계정보
                // 일주일치
                //판단 로직 -> goodBad()
                val dateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())

                val currentDate = Date() // 현재 날짜와 시간
                val calendar = Calendar.getInstance()

                val today = calendar.time
                calendar.add(Calendar.DAY_OF_YEAR, -6) // 6일 전 날짜로 설정
                val sixDaysAgo = calendar.time

                for (i in 0..6) {
                    val filteredData = sensorDataList?.filter { data: SensorData? ->
                        val date = dateFormat.parse(data?.date)
                        date?.after(sixDaysAgo) == true && date?.before(today) == true // 6일 전부터 오늘까지의 데이터만 선택
                    }

                    //(data: List<SensorData>?, interval: Int): Double

                    val averageAirTemp = calculateAverage(
                        filteredData,
                        numDataPointsPerHour
                    ) { it: SensorData -> it.airTemp }
                    val averageAirHumid = calculateAverage(
                        filteredData,
                        numDataPointsPerHour
                    ) { it: SensorData -> it.airHumid }
                    val averageSoilHumid = calculateAverage(
                        filteredData,
                        numDataPointsPerHour
                    ) { it: SensorData -> it.soilHumid }
                    val averageLightIntensity = calculateAverage(
                        filteredData,
                        numDataPointsPerHour
                    ) { it: SensorData -> it.lightIntensity }

                    //println("Statistics for ${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.DAY_OF_MONTH)}:")
                    println("Average Air Temperature: $averageAirTemp")
                    println("Average Air Humidity: $averageAirHumid")
                    println("Average Light Intensity: $averageLightIntensity")
                    println("Average Soil Humidity: $averageSoilHumid")


                    //api로 가져온 적정치와 비교 -> 차이를 보여줌 -> 각 temp, humid, light, sil humid별로 차이 평균
                    var difference: Array<Double> = differenceRealVSApi(averageAirTemp, averageAirHumid,averageLightIntensity, averageSoilHumid)
                    difference.average() //그래프로 그릴 값 하나

                    //var valuesReal: List<Double> = listOf()
                    //valuesReal += difference.average() //api test 이후 이렇게 작성 -> 일단 test를 위해 임의로 values arraylist 선언, 사용

                    calendar.add(Calendar.DAY_OF_YEAR, 1) // 다음 날짜로 이동

                    //val values = ArrayList<Entry>() //차트 데이터 셋에 담겨질 데이터
                    var values = listOf(22, 33, 31, 0, 1, 77, 1) //더미 데이터, 6일 전 부터
                    var labels = listOf("mon", "tue", "wed", "thu", "fri", "sat")

                    drawChart(values, labels)
                }


            } else {
                // 요청이 실패했을 때 처리
            }


        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        //1. test 용도
        val temper: TextView = findViewById(R.id.textView_sensor_temperature)
        val humid: TextView = findViewById(R.id.textView_button_sensor_humidity)
        val light: TextView = findViewById(R.id.textView_button_sensor_light)
        val solidHumid: TextView = findViewById(R.id.textView_button_sensor_soilHumidity)

        temper.text = "온도\n22℃"
        humid.text = "습도\n30%"
        light.text = "조도\n100LUX"
        solidHumid.text = "토양습도\n11%"


        var values = listOf(22, 33, 31, 0, 1, 77, 1) //더미 데이터, 6일 전 부터
        var labels = listOf("mon", "tue", "wed", "thu", "fri", "sat")

        drawChart(values, labels)

        // 2. rest api 사용
        /*
        CoroutineScope(Dispatchers.Main).launch {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://api.nongsaro.go.kr/service/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService: ApiService = retrofit.create(ApiService::class.java)

            val response = withContext(Dispatchers.IO) {
                apiService.getGardenDetail("키값", "디테일번호(품종)")
            }

            val sensorDataList = response.body()
            sensorDataList?.watercycleSummerCode?.let { Log.d("waterCycleSummer", it) }

            //getSensorData()
        }
        */


        // 주어진 데이터 리스트에서 특정 간격으로 데이터를 추출하여 평균 계산

    }

}
