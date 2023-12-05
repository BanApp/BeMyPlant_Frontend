package com.example.bemyplant

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.bemyplant.data.SensorData
import com.example.bemyplant.data.checkIfSensorDataIsLatest
import com.example.bemyplant.network.RetrofitService
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.graphics.Typeface
import android.graphics.Color

class SensorActivity : AppCompatActivity() {
    //private lateinit var binding : ActivitySensorBinding
    private val retrofitService = RetrofitService().apiService
    private lateinit var regenButton: ImageButton
    private lateinit var temperButton: Button
    private lateinit var humidButton: Button
    private lateinit var lightButton: Button
    private lateinit var soilHumidButton: Button
    private lateinit var completeButton: ImageButton
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
        temperButton =findViewById(R.id.Button_sensor_temperature)
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
        }

        // 완료 버튼 클릭 시 메인 화면으로 이동
        completeButton.setOnClickListener{
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
        temperButton.setOnClickListener{
            if (todayTempers.isNullOrEmpty() or todayTempersLabel.isNullOrEmpty()){

                getSensorDatas() // api call

                if(todayTempers.isNullOrEmpty() or todayTempersLabel.isNullOrEmpty() ){
                    // 그래프를 가져올 수 없습니다
                    graphErrText.visibility = View.VISIBLE
                    sensorErrText.visibility = View.INVISIBLE
                    lineChart.visibility = View.INVISIBLE
                }
            }
            drawChart(todayTempers, todayTempersLabel)
        }

        // 습도 버튼 클릭 시 습도 그래프 그리기
        humidButton.setOnClickListener{
            if (todayHumids.isNullOrEmpty()or todayHumidsLabel.isNullOrEmpty() ){

                getSensorDatas() // api call

                if(todayHumids.isNullOrEmpty() or todayHumidsLabel.isNullOrEmpty() ){
                    // 그래프를 가져올 수 없습니다
                    graphErrText.visibility = View.VISIBLE
                    sensorErrText.visibility = View.INVISIBLE
                    lineChart.visibility = View.INVISIBLE
                }
            }
            drawChart(todayHumids, todayHumidsLabel)
        }

        // 조도 버튼 클릭 시 조도 그래프 그리기
        lightButton.setOnClickListener{
            if (todayLights.isNullOrEmpty() or todayLightsLabel.isNullOrEmpty()){

                getSensorDatas() // api call

                if(todayLights.isNullOrEmpty() or todayLightsLabel.isNullOrEmpty() ){
                    // 그래프를 가져올 수 없습니다
                    graphErrText.visibility = View.VISIBLE
                    sensorErrText.visibility = View.INVISIBLE
                    lineChart.visibility = View.INVISIBLE
                }
            }
            drawChart(todayLights, todayLightsLabel)
        }

        // 토양 습도 버튼 클릭 시 조도 그래프 그리기
        soilHumidButton.setOnClickListener{
            if (todaySoilHumids.isNullOrEmpty() or todaySoilHumidsLabel.isNullOrEmpty()){

                getSensorDatas() // api call

                if(todaySoilHumids.isNullOrEmpty() or todaySoilHumidsLabel.isNullOrEmpty() ){
                    // 그래프를 가져올 수 없습니다
                    graphErrText.visibility = View.VISIBLE
                    sensorErrText.visibility = View.INVISIBLE
                    lineChart.visibility = View.INVISIBLE
                }
            }
            drawChart(todaySoilHumids, todaySoilHumidsLabel)
        }
    }


    private fun drawChart(values: List<Double>, labels: List<String>) {
        // 그래프 부분에 '센서 연결을 확인해주세요!' 안보이게
        sensorErrText.visibility = View.INVISIBLE
        graphErrText.visibility = View.INVISIBLE
        lineChart.visibility = View.VISIBLE

        //그래프 그리기
        lineChart.xAxis.valueFormatter= IndexAxisValueFormatter(labels)
        var entries = ArrayList<Entry>()
        for(i in values.indices){
            entries.add(Entry(i.toFloat(), values[i].toFloat()))
        }
        val dataset= LineDataSet(entries, "")
        val data = LineData(dataset)
        val xAxis = lineChart.xAxis
        val yAxis = lineChart.axisLeft

        xAxis.typeface = Typeface.create("@font/notosans_black", Typeface.NORMAL)
        xAxis.textSize = 10F
        yAxis.typeface = Typeface.create("@font/notosans_bold", Typeface.NORMAL)
        yAxis.textSize = 6F
        dataset.setLineWidth(2F); //라인 두께
        dataset.setCircleRadius(2F); // 점 크기
        dataset.setCircleColor(Color.parseColor("#9999FF")); // 점 색깔
        dataset.setDrawCircleHole(false); // 원의 겉 부분 칠할거?
        dataset.setColor(Color.parseColor("#9999CC"));
        lineChart.data = data
        lineChart.invalidate()
        lineChart.data = data
        lineChart.invalidate()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSensorDatas() {
        val sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        val Tag: String = "sensor"
        Log.d(Tag, "token: $token")

        // 토큰 부재 시 초기화면(MJ_main)으로 전환
        if (token.isNullOrEmpty()){
            val homeIntent = Intent(this@SensorActivity, MJ_MainActivity::class.java)
            startActivity(homeIntent)
        }

        val collectingInterval = 1 // 데이터 수집 주기 (분 단위)
        val minutesFromMidnight = calculateMinutesFromMidnight()

        // 현재 시간 기준으로 0시부터의 경과 시간을 계산하여 데이터 가져오기
        val dataCountPerDay = calculateDataCountPerDay(collectingInterval)
        val dataNumsToCollect = dataCountPerDay - (minutesFromMidnight / collectingInterval) // 샌서 가져올 횟수


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
                val sensorDataList = response.body()
                if (!sensorDataList.isNullOrEmpty()) {
                    val averagesLists = calculateHourlyAverages(sensorDataList)

                    todaySoilHumids = averagesLists.todaySoilHumids
                    todayTempers = averagesLists.todayTempers
                    todayHumids = averagesLists.todayHumids
                    todayLights = averagesLists.todayLights
                    todaySoilHumidsLabel = averagesLists.todaySoilHumidsLabel
                    todayTempersLabel = averagesLists.todayTempersLabel
                    todayHumidsLabel = averagesLists.todayHumidsLabel
                    todayLightsLabel = averagesLists.todayLightsLabel
                }
            }
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateHourlyAverages(sensorDataList: List<SensorData>): HourlyAveragesLists {
        val hourlyAverages = mutableMapOf<Int, SensorHourlyAverages>()

        // 데이터 그룹화 및 평균 계산
        for (sensorData in sensorDataList) {
            val date = LocalDateTime.parse(sensorData.date, DateTimeFormatter.ISO_DATE_TIME)
            val hour = date.hour

            val currentAverage = hourlyAverages[hour] ?: SensorHourlyAverages(hour, 0.0, 0.0, 0.0, 0.0)

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
            val currentAverage = hourlyAverages[hour] ?: SensorHourlyAverages(hour, 0.0, 0.0, 0.0, 0.0)

            // 센서 종류별 평균 계산
            val totalCount = sensorDataList.count { LocalDateTime.parse(it.date, DateTimeFormatter.ISO_DATE_TIME).hour == hour }
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
    

    // setNewSensorData function calls sensor API & set textViews(temper, humid, light, soilHumid).
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun setNewSensorData() {
        val sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        val Tag: String = "sensor"
        Log.d(Tag, "token: $token")

        // 토큰 부재 시 초기화면(MJ_main)으로 전환
        if (token.isNullOrEmpty()){
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


        if (response.isSuccessful) {
            // 현재 센서가 최신의 것인지 check
            checkIfSensorDataIsLatest(this) { isLatest ->
                if (!isLatest) {
                    // 최신의 데이터가 아니라면 ??? 으로 설정, 그래프 부분에 '센서 연결을 확인해주세요!'
                    sensorErrText.visibility = View.VISIBLE
                    graphErrText.visibility = View.INVISIBLE
                    lineChart.visibility = View.INVISIBLE

                    runOnUiThread {
                        humidButton.text = "습도\n" + "??"
                        lightButton.text =
                            "조도\n" + "??"
                        soilHumidButton.text =
                            "토양습도\n" + "??"
                        temperButton.text = "온도\n" + "??"

                        // 화면 갱신
                        humidButton.invalidate()
                        lightButton.invalidate()
                        soilHumidButton.invalidate()
                        temperButton.invalidate()
                    }

                } else {
                    // 그래프 부분에 '센서 연결을 확인해주세요!' 안보이게
                    sensorErrText.visibility = View.INVISIBLE
                    graphErrText.visibility = View.INVISIBLE
                    lineChart.visibility = View.VISIBLE

                    val sensorDataList = response.body()
                    // 1. date값
                    //가장 최근 시간
                    if (!sensorDataList.isNullOrEmpty()) {
                        val mostRecentData = sensorDataList.firstOrNull()

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
        }
    }




    // 렌더링 시 한 번에 센서 데이터 조회 -> 센서 정보, 그래프 한 번에 그림 (이전 아키텍처 구조)
    /*
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
    */

}
