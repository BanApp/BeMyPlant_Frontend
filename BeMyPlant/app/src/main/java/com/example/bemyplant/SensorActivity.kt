package com.example.bemyplant

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bemyplant.data.SensorData
import com.example.bemyplant.network.RetrofitService
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class SensorActivity : AppCompatActivity() {
    //private lateinit var binding : ActivitySensorBinding
    private val retrofitService = RetrofitService().apiService
    private lateinit var regenButton: ImageButton
    private lateinit var temperButton: Button
    private lateinit var humidButton: Button
    private lateinit var lightButton: Button
    private lateinit var soilHumidButton: Button
    private lateinit var completeButton: Button
    private lateinit var sensorErrText: TextView
    private lateinit var graphErrText: TextView
    private lateinit var lineChart: LineChart

    private lateinit var todaySoilHumids: List<Double>
    private lateinit var todaySoilHumidsLabel: List<String>

    private lateinit var todayTempers: List<Double>
    private lateinit var todayTempersLabel: List<String>

    private lateinit var todayHumids: List<Double>
    private lateinit var todayHumidsLabel: List<String>

    private lateinit var todayLights: List<Double>
    private lateinit var todayLightsLabel: List<String>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        regenButton = findViewById(R.id.imageButton_sensor_regen)
        temperButton = findViewById(R.id.Button_sensor_temperature)
        humidButton = findViewById(R.id.Button_sensor_humidity)
        lightButton = findViewById(R.id.Button_sensor_light)
        soilHumidButton = findViewById(R.id.Button_sensor_soilHumidity)
        completeButton = findViewById(R.id.imageButton_sensor_complete)
        sensorErrText = findViewById(R.id.textView_sensor_sensorErr)
        graphErrText = findViewById(R.id.textView_sensor_graphErr)
        lineChart = findViewById(R.id.chart_sensor_sensorChart)

        todaySoilHumids = emptyList()
        todaySoilHumidsLabel = emptyList()

        todayTempers = emptyList()
        todayTempersLabel = emptyList()

        todayHumids = emptyList()
        todayHumidsLabel = emptyList()

        todayLights = emptyList()
        todayLightsLabel = emptyList()

        CoroutineScope(Dispatchers.Main).launch {
            // 화면에 접근할 때 센서 데이터 설정
            setNewSensorData()
            getSensorDatas() // api call
        }

        // 완료 버튼 클릭 시 메인 화면으로 이동
        completeButton.setOnClickListener {
            val homeIntent = Intent(this@SensorActivity, MainActivity::class.java)
            startActivity(homeIntent)
        }

        // regen 버튼 클릭 시 센서 정보 갱신
        regenButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                setNewSensorData()
                //getHealthInfo()로 health data n개 추출 -> drawChart()로 그래프 그리기
            }
        }

        // 온도 버튼 클릭 시 온도 그래프 그리기
        temperButton.setOnClickListener {
            if (todayTempers.isNullOrEmpty() or todayTempersLabel.isNullOrEmpty()) {

                getSensorDatas() // api call

                if (todayTempers.isNullOrEmpty() or todayTempersLabel.isNullOrEmpty()) {
                    // 그래프를 가져올 수 없습니다
                    graphErrText.visibility = View.VISIBLE
                    sensorErrText.visibility = View.INVISIBLE
                    lineChart.visibility = View.INVISIBLE
                }
                else{
                    // NaN 값을 제거하여 새로운 리스트 생성
                    val filteredValues = todayTempers.filter { !it.isNaN() }

                    // NaN에 해당하는 라벨도 제거
                    val filteredLabels = todayTempersLabel.take(filteredValues.size)

                    // 그래프 그리기
                    drawChart(filteredValues, filteredLabels, "온도")
                    //drawChart(todayTempers, todayTempersLabel, "온도")
                }
            }
            else{
                // NaN 값을 제거하여 새로운 리스트 생성
                val filteredValues = todayTempers.filter { !it.isNaN() }

                // NaN에 해당하는 라벨도 제거
                val filteredLabels = todayTempersLabel.take(filteredValues.size)

                // 그래프 그리기
                drawChart(filteredValues, filteredLabels, "온도")
                // drawChart(todayTempers, todayTempersLabel, "온도")
            }
        }

        // 습도 버튼 클릭 시 습도 그래프 그리기
        humidButton.setOnClickListener {
            if (todayHumids.isNullOrEmpty() or todayHumidsLabel.isNullOrEmpty()) {

                getSensorDatas() // api call

                if (todayHumids.isNullOrEmpty() or todayHumidsLabel.isNullOrEmpty()) {
                    // 그래프를 가져올 수 없습니다
                    graphErrText.visibility = View.VISIBLE
                    sensorErrText.visibility = View.INVISIBLE
                    lineChart.visibility = View.INVISIBLE
                }
                else{
                    // NaN 값을 제거하여 새로운 리스트 생성
                    val filteredValues = todayHumids.filter { !it.isNaN() }

                    // NaN에 해당하는 라벨도 제거
                    val filteredLabels = todayHumidsLabel.take(filteredValues.size)

                    // 그래프 그리기
                    drawChart(filteredValues, filteredLabels, "습도")
                    //drawChart(todayHumids, todayHumidsLabel, "습도")
                }
            }
            else{
                // NaN 값을 제거하여 새로운 리스트 생성
                val filteredValues = todayHumids.filter { !it.isNaN() }

                // NaN에 해당하는 라벨도 제거
                val filteredLabels = todayHumidsLabel.take(filteredValues.size)

                // 그래프 그리기
                drawChart(filteredValues, filteredLabels, "습도")
                //drawChart(todayHumids, todayHumidsLabel, "습도")
            }
        }

        // 조도 버튼 클릭 시 조도 그래프 그리기
        lightButton.setOnClickListener {
            if (todayLights.isNullOrEmpty() or todayLightsLabel.isNullOrEmpty()) {

                getSensorDatas() // api call

                if (todayLights.isNullOrEmpty() or todayLightsLabel.isNullOrEmpty()) {
                    // 그래프를 가져올 수 없습니다
                    graphErrText.visibility = View.VISIBLE
                    sensorErrText.visibility = View.INVISIBLE
                    lineChart.visibility = View.INVISIBLE
                }
                else{
                    // NaN 값을 제거하여 새로운 리스트 생성
                    val filteredValues = todayLights.filter { !it.isNaN() }

                    // NaN에 해당하는 라벨도 제거
                    val filteredLabels = todayLightsLabel.take(filteredValues.size)

                    // 그래프 그리기
                    drawChart(filteredValues, filteredLabels, "조도")
                    //drawChart(todayLights, todayLightsLabel, "조도")
                }
            }
            else{
                // NaN 값을 제거하여 새로운 리스트 생성
                val filteredValues = todayLights.filter { !it.isNaN() }

                // NaN에 해당하는 라벨도 제거
                val filteredLabels = todayLightsLabel.take(filteredValues.size)

                // 그래프 그리기
                drawChart(filteredValues, filteredLabels, "조도")
                //drawChart(todayLights, todayLightsLabel, "조도")
            }
        }

        // 토양 습도 버튼 클릭 시 조도 그래프 그리기
        soilHumidButton.setOnClickListener {
            if (todaySoilHumids.isNullOrEmpty() or todaySoilHumidsLabel.isNullOrEmpty()) {

                getSensorDatas() // api call

                if (todaySoilHumids.isNullOrEmpty() or todaySoilHumidsLabel.isNullOrEmpty()) {
                    // 그래프를 가져올 수 없습니다
                    graphErrText.visibility = View.VISIBLE
                    sensorErrText.visibility = View.INVISIBLE
                    lineChart.visibility = View.INVISIBLE
                }
                else{
                    // NaN 값을 제거하여 새로운 리스트 생성
                    val filteredValues = todaySoilHumids.filter { !it.isNaN() }

                    // NaN에 해당하는 라벨도 제거
                    val filteredLabels = todaySoilHumidsLabel.take(filteredValues.size)

                    // 그래프 그리기
                    drawChart(filteredValues, filteredLabels, "토양습도")
                    //drawChart(todaySoilHumids, todaySoilHumidsLabel, "토양습도")
                }
            }
            else{
                // NaN 값을 제거하여 새로운 리스트 생성
                val filteredValues = todaySoilHumids.filter { !it.isNaN() }

                // NaN에 해당하는 라벨도 제거
                val filteredLabels = todaySoilHumidsLabel.take(filteredValues.size)

                // 그래프 그리기
                drawChart(filteredValues, filteredLabels, "토양습도")
                //drawChart(todaySoilHumids, todaySoilHumidsLabel, "토양습도")
            }
        }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSensorDatas() {
        val sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        val Tag: String = "sensor"
        Log.d(Tag, "token: $token")

        // 토큰 부재 시 초기화면(MJ_main)으로 전환
        if (token.isNullOrEmpty()) {
            val homeIntent = Intent(this@SensorActivity, MJ_MainActivity::class.java)
            startActivity(homeIntent)
        }

        val collectingInterval = 1 // 데이터 수집 주기 (분 단위)
        val minutesFromMidnight = calculateMinutesFromMidnight()

        // 현재 시간 기준으로 0시부터의 경과 시간을 계산하여 데이터 가져오기
        val dataCountPerDay = calculateDataCountPerDay(collectingInterval)
        val dataNumsToCollect =
            dataCountPerDay - (minutesFromMidnight / collectingInterval) // 샌서 가져올 횟수


        CoroutineScope(Dispatchers.IO).launch {
            // api call to get sensor data
            val response = retrofitService.getSensorData(dataNumsToCollect, "Bearer " + token)
            // HTTP 오류 코드에 대한 처리
            if (response.code() == 401) {
                // 401 Unauthorized 오류 처리
                // 첫 화면으로 (로그인 시도 화면) 이동
                val homeIntent = Intent(this@SensorActivity, MJ_MainActivity::class.java)
                startActivity(homeIntent)
            } else {
                // TODO: 센서 데이터 api call - 다른 HTTP 오류 코드에 대한 처리
            }

            val Tag: String = "sensor"
            Log.d(Tag, "sensor raw-response: $response")

            if (response.isSuccessful) {
                var sensorDataListToday:ArrayList<SensorData> = ArrayList(0)
                val sensorDataList = response.body()
                if (!sensorDataList.isNullOrEmpty()) {
                    for (sensorData in sensorDataList) {
                        sensorData?.soilHumid = (sensorData?.soilHumid)?.div(100)!!
                        val date =
                            LocalDateTime.parse(sensorData.date, DateTimeFormatter.ISO_DATE_TIME)
                        if (!checkDateMatch(date)) {
                            // 현재 날짜에 해당하는 센서값이 아님
                            continue
                        }
                        sensorDataListToday.add(sensorData)
                    }

                    Log.d("chart sensorDataListToday", sensorDataListToday.toString())
                    if (!sensorDataListToday.isNullOrEmpty()){
                        val averagesLists = calculateHourlyAverages(sensorDataListToday)
                        Log.d("chart averagesLists", averagesLists.toString())

                        todaySoilHumids = averagesLists.todaySoilHumids
                        todayTempers = averagesLists.todayTempers
                        todayHumids = averagesLists.todayHumids
                        todayLights = averagesLists.todayLights
                        todaySoilHumidsLabel = averagesLists.todaySoilHumidsLabel
                        todayTempersLabel = averagesLists.todayTempersLabel
                        todayHumidsLabel = averagesLists.todayHumidsLabel
                        todayLightsLabel = averagesLists.todayLightsLabel
                    }
                    else{
                        todaySoilHumids = emptyList()
                        todayTempers = emptyList()
                        todayHumids = emptyList()
                        todayLights = emptyList()
                        todaySoilHumidsLabel = emptyList()
                        todayTempersLabel = emptyList()
                        todayHumidsLabel = emptyList()
                        todayLightsLabel = emptyList()
                    }
                }
            }
        }

    }
    // setNewSensorData function calls sensor API & set textViews(temper, humid, light, soilHumid).
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun setNewSensorData() {
        val sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        val Tag: String = "sensor"
        Log.d(Tag, "token: $token")

        // 토큰 부재 시 초기화면(MJ_main)으로 전환
        if (token.isNullOrEmpty()) {
            val homeIntent = Intent(this@SensorActivity, MJ_MainActivity::class.java)
            startActivity(homeIntent)
        }
        // api call to get sensor data
        val response = retrofitService.getSensorData(1, "Bearer " + token)
        // HTTP 오류 코드에 대한 처리
        if (response.code() == 401) {
            // 401 Unauthorized 오류 처리
            // 첫 화면으로 (로그인 시도 화면) 이동
            val homeIntent = Intent(this@SensorActivity, MJ_MainActivity::class.java)
            startActivity(homeIntent)
        } else {
            // TODO: 센서 데이터 api call - 다른 HTTP 오류 코드에 대한 처리
        }

        Log.d(Tag, "sensor raw-response: $response")


//        checkIfSensorDataIsLatest(this) { isLatest ->
//            if (!isLatest) {
//                Log.d("latest", "not latest")
//                // 최신의 데이터가 아니라면 ??? 으로 설정, 그래프 부분에 '센서 연결을 확인해주세요!'
//                sensorErrText.visibility = View.VISIBLE
//                graphErrText.visibility = View.INVISIBLE
//                lineChart.visibility = View.INVISIBLE
//
//                runOnUiThread {
//                    humidButton.text = "습도\n" + "??"
//                    lightButton.text =
//                        "조도\n" + "??"
//                    soilHumidButton.text =
//                        "토양습도\n" + "??"
//                    temperButton.text = "온도\n" + "??"
//
//                    // 화면 갱신
//                    humidButton.invalidate()
//                    lightButton.invalidate()
//                    soilHumidButton.invalidate()
//                    temperButton.invalidate()
//                }
//            } else {
//                Log.d("latest", "not latest")
//                // 그래프 부분에 '센서 연결을 확인해주세요!' 안보이게
//                sensorErrText.visibility = View.INVISIBLE
//                graphErrText.visibility = View.INVISIBLE
//                //lineChart.visibility = View.VISIBLE
//                if (response.isSuccessful) {
//                    val sensorDataList = response.body()
//                    // 1. date값
//                    //가장 최근 시간
//                    if (!sensorDataList.isNullOrEmpty()) {
//                        val mostRecentData = sensorDataList.firstOrNull()
//                        mostRecentData?.soilHumid = (mostRecentData?.soilHumid)?.div(100)!!
//
//                        Log.d(Tag, "sensor airtemp: ${mostRecentData?.airTemp.toString()}")
//                        Log.d(Tag, "sensor humid: ${mostRecentData?.airHumid.toString()}")
//                        Log.d(Tag, "sensor light: ${mostRecentData?.lightIntensity.toString()}")
//                        Log.d(Tag, "sensor soil-humid: ${mostRecentData?.soilHumid.toString()}")
//
//                        runOnUiThread {
//                            humidButton.text =
//                                "습도\n" + String.format("%.2f%%", mostRecentData?.airHumid)
//                            lightButton.text =
//                                "조도\n" + String.format("%.2fLUX", mostRecentData?.lightIntensity)
//                            soilHumidButton.text =
//                                "토양습도\n" + String.format("%.2f%%", mostRecentData?.soilHumid)
//                            temperButton.text =
//                                "온도\n" + String.format("%.2f℃", mostRecentData?.airTemp)
//
//                            // 화면 갱신
//                            humidButton.invalidate()
//                            lightButton.invalidate()
//                            soilHumidButton.invalidate()
//                            temperButton.invalidate()
//                        }
//                    }
//
//                }
//            }
//        }

        Log.d("latest", "not latest")
        // 그래프 부분에 '센서 연결을 확인해주세요!' 안보이게
        sensorErrText.visibility = View.INVISIBLE
        graphErrText.visibility = View.INVISIBLE
        //lineChart.visibility = View.VISIBLE
        if (response.isSuccessful) {
            val sensorDataList = response.body()
            // 1. date값
            //가장 최근 시간
            if (!sensorDataList.isNullOrEmpty()) {
                val mostRecentData = sensorDataList.firstOrNull()
                mostRecentData?.soilHumid = (mostRecentData?.soilHumid)?.div(100)!!

                Log.d(Tag, "sensor airtemp: ${mostRecentData?.airTemp.toString()}")
                Log.d(Tag, "sensor humid: ${mostRecentData?.airHumid.toString()}")
                Log.d(Tag, "sensor light: ${mostRecentData?.lightIntensity.toString()}")
                Log.d(Tag, "sensor soil-humid: ${mostRecentData?.soilHumid.toString()}")

                runOnUiThread {
                    humidButton.text =
                        "습도\n" + String.format("%.2f%%", mostRecentData?.airHumid)
                    lightButton.text =
                        "조도\n" + String.format("%.2fLUX", mostRecentData?.lightIntensity)
                    soilHumidButton.text =
                        "토양습도\n" + String.format("%.2f%%", mostRecentData?.soilHumid)
                    temperButton.text =
                        "온도\n" + String.format("%.2f℃", mostRecentData?.airTemp)

                    // 화면 갱신
                    humidButton.invalidate()
                    lightButton.invalidate()
                    soilHumidButton.invalidate()
                    temperButton.invalidate()
                }
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkDateMatch(sensorDate: LocalDateTime): Boolean {
        // 현재 날짜 구하기
        val currentDate = LocalDate.now()

        // 년, 월, 일 비교
        return currentDate.year == sensorDate.year &&
                currentDate.month == sensorDate.month &&
                currentDate.dayOfMonth == sensorDate.dayOfMonth
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateHourlyAverages(sensorDataList: List<SensorData>): HourlyAveragesLists {
        val hourlyAverages = mutableMapOf<Int, SensorHourlyAverages>()

        // 데이터 그룹화 및 평균 계산
        for (sensorData in sensorDataList) {
            val date = LocalDateTime.parse(sensorData.date, DateTimeFormatter.ISO_DATE_TIME)
            if (!checkDateMatch(date)) {
                // 현재 날짜에 해당하는 센서값이 아님
                continue
            }
            val hour = date.hour

            val currentAverage =
                hourlyAverages[hour] ?: SensorHourlyAverages(hour, 0.0, 0.0, 0.0, 0.0)

            hourlyAverages[hour] = currentAverage.copy(
                airTemp = currentAverage.airTemp + sensorData.airTemp,
                airHumid = currentAverage.airHumid + sensorData.airHumid,
                soilHumid = currentAverage.soilHumid + sensorData.soilHumid,
                lightIntensity = currentAverage.lightIntensity + sensorData.lightIntensity
            )
        }

        // 결과 리스트 생성
        val todaySoilHumids = mutableListOf<Double>()
        val todayTempers = mutableListOf<Double>()
        val todayHumids = mutableListOf<Double>()
        val todayLights = mutableListOf<Double>()
        val todaySoilHumidsLabel = mutableListOf<String>()
        val todayTempersLabel = mutableListOf<String>()
        val todayHumidsLabel = mutableListOf<String>()
        val todayLightsLabel = mutableListOf<String>()

        for (hour in 0..23) {
            val currentAverage =
                hourlyAverages[hour] ?: SensorHourlyAverages(hour, 0.0, 0.0, 0.0, 0.0)

            // 센서 종류별 평균 계산
            val totalCount = sensorDataList.count {
                LocalDateTime.parse(
                    it.date,
                    DateTimeFormatter.ISO_DATE_TIME
                ).hour == hour
            }
            val average = currentAverage.copy(
                airTemp = currentAverage.airTemp / totalCount,
                airHumid = currentAverage.airHumid / totalCount,
                soilHumid = currentAverage.soilHumid / totalCount,
                lightIntensity = currentAverage.lightIntensity / totalCount
            )

            todaySoilHumids.add(average.soilHumid)
            todayTempers.add(average.airTemp)
            todayHumids.add(average.airHumid)
            todayLights.add(average.lightIntensity)
            todaySoilHumidsLabel.add(hour.toString() + "시")
            todayTempersLabel.add(hour.toString() + "시")
            todayHumidsLabel.add(hour.toString() + "시")
            todayLightsLabel.add(hour.toString() + "시")
        }

        return HourlyAveragesLists(
            todaySoilHumids, todayTempers, todayHumids, todayLights,
            todaySoilHumidsLabel, todayTempersLabel, todayHumidsLabel, todayLightsLabel
        )
    }

    private fun drawChart(values: List<Double>, labels: List<String>, label: String) {
        Log.d("chart value:", values.toString())
        Log.d("chart label:", labels.toString())

        // 그래프 부분에 '센서 연결을 확인해주세요!' 안보이게
        sensorErrText.visibility = View.INVISIBLE
        graphErrText.visibility = View.INVISIBLE
        lineChart.visibility = View.VISIBLE

        lineChart.description.isEnabled = false

        // 그래프 그리기
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        var entries = ArrayList<Entry>()
        for (i in values.indices) {
            entries.add(Entry(i.toFloat(), values[i].toFloat()))
        }

        // 아래 부분 색상 및 투명도 설정
        val dataset = LineDataSet(entries, label)
        val xAxis = lineChart.xAxis
        val yAxis = lineChart.axisLeft
        xAxis.typeface = Typeface.create("@font/notosans_black", Typeface.NORMAL)
        yAxis.typeface = Typeface.create("@font/notosans_black", Typeface.NORMAL)
        xAxis.textSize = 10F
        yAxis.textSize = 6F
        dataset.setLineWidth(2F)
        dataset.setCircleRadius(2F)
        dataset.setCircleColor(Color.parseColor("#9999FF"))
        dataset.setDrawCircles(false)
        dataset.setColor(Color.parseColor("#9999CC"))

        dataset.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataset.cubicIntensity = 0.2f

        //dataset.color = Color.BLUE
//        dataset.setCircleColor(Color.BLUE)
//        dataset.setDrawCircles(true)
//        dataset.setDrawFilled(true)
        dataset.fillDrawable = ContextCompat.getDrawable(this, R.drawable.gradient_fill)
        dataset.fillAlpha = 50
        dataset.highLightColor = Color.GREEN

        // 차트의 기준선 설정
        val baseLine = values.firstOrNull()?.toFloat() ?: 0f
        lineChart.axisLeft.axisMinimum = baseLine - 10f // 기준선보다 약간 아래로 설정

        val data = LineData(dataset)
        lineChart.data = data
        lineChart.invalidate()
    }


    // 0시를 기준으로 몇 분이 지났는지 측정
    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateMinutesFromMidnight(): Int {
        val now = LocalDateTime.now()
        val midnight = now.toLocalDate().atStartOfDay()
        val minutesFromMidnight = midnight.until(now, java.time.temporal.ChronoUnit.MINUTES)
        return minutesFromMidnight.toInt()
    }

    // 센서 그래프를 위해 몇 개의 데이터를 가져와야 하는지 연산하는 함수
    fun calculateDataCountPerDay(collectingInterval: Int): Int {
        val minutesInDay = 24 * 60
        return minutesInDay / collectingInterval
    }

    data class SensorHourlyAverages(
        val hour: Int,
        val airTemp: Double,
        val airHumid: Double,
        val soilHumid: Double,
        val lightIntensity: Double
    )

    data class HourlyAveragesLists(
        val todaySoilHumids: List<Double>,
        val todayTempers: List<Double>,
        val todayHumids: List<Double>,
        val todayLights: List<Double>,
        val todaySoilHumidsLabel: List<String>,
        val todayTempersLabel: List<String>,
        val todayHumidsLabel: List<String>,
        val todayLightsLabel: List<String>
    )

}