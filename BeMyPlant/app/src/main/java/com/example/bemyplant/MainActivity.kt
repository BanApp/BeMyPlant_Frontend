package com.example.bemyplant

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.bemyplant.data.StatusData
import com.example.bemyplant.data.checkIfSensorDataIsLatest
import com.example.bemyplant.fragment.FlowerIdFragment
import com.example.bemyplant.network.RetrofitService
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class PlantImage(val resourceId: Int, val description: String) // TODO: DB 연동 후 삭제


class MainActivity : AppCompatActivity() {
    private lateinit var currentPlantImage: PlantImage // TODO: DB 연동 후 삭제
    private lateinit var regenerateButton: ImageButton
    private lateinit var mainFlower: ImageButton
    private lateinit var plantName: TextView
    private lateinit var plantRace: String
    private lateinit var plantRegistration: String
    private lateinit var statusText: TextView
    private lateinit var statusImages: Array<ImageView>
    private lateinit var strangeConText: TextView
    private lateinit var strangeCondition: LinearLayout
    private val retrofitService = RetrofitService().apiService2

    // ----------- 상태에 따른 이미지 및 텍스트 변경
    private fun updateStatus() {
        val statusData = StatusData("Seoul")
        val sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = retrofitService.getWeatherAndStatus(statusData, "Bearer $token")
                if (response.isSuccessful){
                    launch(Dispatchers.Main) {
                        // 상태에 따른 이미지 변경
                        val statusResponse = response.body()
                        val statusTemp = statusResponse?.status
                        val strangeTemp = statusResponse?.most_important_feature
                        if (statusTemp != 0){
                            val statusImageResource = arrayOf(R.drawable.good_status1 , R.drawable.good_status2, R.drawable.good_status3)
                            val randomIndex = (0 until statusImageResource.size).random()
                            for (image in statusImages){
                                image.setImageResource(statusImageResource[randomIndex])
                            }
                        } else {
                            val statusImageResource = arrayOf(R.drawable.bad_status)
                            for (image in statusImages){
                                image.setImageResource(statusImageResource[0])
                            }
                        }

                        //상태에 따른 텍스트 변경
                        if (statusTemp != 0) {
                            statusText.text = "Good"
                            strangeConText.visibility = View.INVISIBLE

                        } else {
                            statusText.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.coral))
                            statusText.text = "Bad"
                            strangeCondition.visibility = View.VISIBLE

                            when (strangeTemp) {
                                "airHumid" -> strangeConText.text = "공기습도이상"
                                "airTemp" -> strangeConText.text = "온도이상"
                                "lightIntensity" -> strangeConText.text = "조도이상"
                                "soilHumid" -> strangeConText.text = "토양습도이상"
                            }
                        }
                    }
                    }else{
                    val errorBody = response.errorBody()?.string()
                    Log.e("Error_Response", errorBody ?: "error body X")
                }

            }catch (e:Exception){
                Log.e("API_Connection", "API 연결 실패")
                e.printStackTrace()


            }
        }
    }

    fun updateMainFlower(newPlantImageResId: Int) {
        mainFlower.setImageResource(newPlantImageResId) // TODO: DB 연동 후 삭제

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        checkIfSensorDataIsLatest(this) { isLatest ->
            if (!isLatest) {
                updateSensorError()
            } else {
                updateStatus()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        regenerateButton = findViewById<ImageButton>(R.id.regenerateButton)
        mainFlower = findViewById<ImageButton>(R.id.mainFlower)
        plantName = findViewById<TextView>(R.id.textView_main_flowerName)
        statusText = findViewById<TextView>(R.id.textView_main_healthValue)
        strangeConText = findViewById<TextView>(R.id.strangeConText)
        strangeCondition = findViewById<LinearLayout>(R.id.strangeCondition)
        statusImages = arrayOf(
            findViewById<ImageView>(R.id.statusImage1),
            findViewById<ImageView>(R.id.statusImage2),
            findViewById<ImageView>(R.id.statusImage3),
        )

        // main image 설정
        //mainFlower.setImageResource(R.drawable.flower)
        //mainFlower.setImageResource(R.drawable.delete_plant)
        //mainFlower.setImageResource(R.drawable.sea_otter)
        //mainFlower.setImageResource(R.drawable.test_img)
        //R.drawable.flower
        // TODO: (정현) 식물 DB 조회 후 렌더링 (D+Day, 식물 이미지, 식물 이름)
        //  R.id.textView_main_dDayValue, R.id.mainFlower, R.id.textView_main_flowerName
        //  렌더링하지 않아도, 일단 DB에서 받아온 값은 모두 변수에 저장해주세요(단, 주민등록번호의 경우 반드시 plantRegistration에 저장하고, 품종은 plantRace에 저장해주세요,...) (다른 화면으로 이동 시 데이터 넘길때 사용)


        //-----------이전 화면에서 넘어오는 이미지 값이 있다면 해당 값으로 이미지 수정
        //currentPlantImage = PlantImage(R.drawable.delete_plant, "Default Image") // TODO: DB 연동 후 삭제
        currentPlantImage = PlantImage(R.drawable.flower, "Default Image")

        // 새로고침 버튼
        regenerateButton.setOnClickListener {
            checkIfSensorDataIsLatest(this) { isLatest ->
                if (!isLatest) {
                    updateSensorError()
                } else {
                    updateStatus()
                }
            }
        }


        val screenFrame = findViewById<FrameLayout>(R.id.screenFrame)

        val newPlantImageResId = intent.getIntExtra("newPlantImageResId", 0) // 다른 화면에서 전달되는 이미지
        if (newPlantImageResId != 0) { // 현재 이미지와 다른 이미지값이 들어온다면
            currentPlantImage = PlantImage(newPlantImageResId, "Custom Image")
            updateMainFlower(newPlantImageResId)
        }
        //----------메인화면에서 식물 이미지 값에 따라 특정 화면 전환
        mainFlower.setOnClickListener {
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            when (currentPlantImage.resourceId){//resources.getIdentifier("mainFlower", "id", this.packageName)) { //currentPlantImage.resourceId
                R.drawable.delete_plant -> {

                    //val fragment = PlantRegisterFragment()
                    /*fragmentTransaction.replace(R.id.screenFrame, fragment) // R.id.fragmentContainer는 Fragment를 표시할 레이아웃의 ID입니다.
                    fragmentTransaction.addToBackStack(null) // 백 스택에 추가 (선택 사항)
                    fragmentTransaction.commit()*/

                    val plantRegisterIntent = Intent(this@MainActivity, PlantImageTempActivity::class.java)
                    startActivity(plantRegisterIntent)

                    //fragmentTransaction.add(R.id.screenFrame, fragment)
                    //fragmentTransaction.addToBackStack(null)
                    //fragmentTransaction.commit()
                    //screenFrame!!.bringToFront()

                }
                else -> {
                    val bundle = Bundle()
                    bundle.putParcelable("plantImage", mainFlower.drawable.toBitmap())
                    bundle.putString("plantName", plantName.text.toString())
                    bundle.putString("plantRace", plantRace)
                    bundle.putString("plantRegistration", plantRegistration)

                    val fragment = FlowerIdFragment()
                    fragment.arguments = bundle
                    fragmentTransaction.add(R.id.screenFrame, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                    screenFrame.bringToFront()

                }

            }

        }


        //------------------------상태에 관한 텍스트 클릭시 , 센서 화면으로 이동
        //TODO: 새로고침 버튼 구현
        val healthText = findViewById<TextView>(R.id.textView_main_healthValue)
        val healthTitle = findViewById<TextView>(R.id.textView_main_healthFrame)

        healthText.setOnClickListener {
            // "LinearLayout" 클릭 시 SensorActivity로 이동
            val sensorIntent = Intent(this@MainActivity, SensorActivity::class.java)
            startActivity(sensorIntent)
        }
        healthTitle.setOnClickListener{
            val sensorIntent = Intent(this@MainActivity, SensorActivity::class.java)
            startActivity(sensorIntent)

        }
        //------------------------하단바

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation_main_menu)
        bottomNavigationView.selectedItemId = R.id.menu_home

        val menuView = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
//        val selectedItemIndex = 1
//
//        menuView.getChildAt(selectedItemIndex).performClick()

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.menu_home -> {
                    // "홈" 메뉴 클릭 시 MainActivity로 이동
                    val homeIntent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(homeIntent)
                    true
                }

                R.id.menu_setting -> {
                    // "setting" 메뉴 클릭 시 SettingActivity로 이동
                    val boardIntent = Intent(this@MainActivity, SettingActivity::class.java)
                    startActivity(boardIntent)
                    true
                }

                R.id.menu_chat -> {
                    // "채팅" 메뉴 클릭 시 ChatActivity로 이동
                    val chatIntent = Intent(this@MainActivity, ChatActivity::class.java)
                    startActivity(chatIntent)
                    true
                }

                R.id.menu_diary -> {
                    // "일기" 메뉴 클릭 시 DiaryActivity로 이동
                    val diaryIntent = Intent(this@MainActivity, DiaryActivity::class.java)
                    startActivity(diaryIntent)
                    true
                }

                else -> false
            }
        }

    }

    private fun updateSensorError() {
        statusText.text = "???"
        for (image in statusImages){
            image.visibility = View.INVISIBLE
        }
        strangeCondition.visibility = View.VISIBLE
        strangeConText.visibility = View.VISIBLE
        strangeConText.text = "센서 연결을 다시 진행해주세요!"

    }
}
