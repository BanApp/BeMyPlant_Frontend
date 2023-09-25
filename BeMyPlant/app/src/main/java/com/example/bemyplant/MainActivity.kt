package com.example.bemyplant

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.bemyplant.fragment.FlowerIdFragment
import com.example.bemyplant.fragment.PlantRegisterFragment
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

data class PlantImage(val resourceId: Int, val description: String) // TODO: DB 연동 후 삭제


class MainActivity : AppCompatActivity() {
    private lateinit var currentPlantImage: PlantImage // TODO: DB 연동 후 삭제

    private lateinit var mainFlower: ImageButton
    private lateinit var plantName: TextView
    private lateinit var plantRace: String
    private lateinit var plantRegistration: String

    fun updateMainFlower(newPlantImageResId: Int) {
        mainFlower.setImageResource(newPlantImageResId) // TODO: DB 연동 후 삭제

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainFlower = findViewById<ImageButton>(R.id.mainFlower)
        plantName = findViewById<TextView>(R.id.textView_main_flowerName)

        mainFlower.setImageResource(R.drawable.delete_plant)
        //R.drawable.flower
        // TODO: (정현) 식물 DB 조회 후 렌더링 (D+Day, 식물 이미지, 식물 이름)
        //  R.id.textView_main_dDayValue, R.id.mainFlower, R.id.textView_main_flowerName
        //  렌더링하지 않아도, 일단 DB에서 받아온 값은 모두 변수에 저장해주세요(단, 주민등록번호의 경우 반드시 plantRegistration에 저장하고, 품종은 plantRace에 저장해주세요,...) (다른 화면으로 이동 시 데이터 넘길때 사용)

        //-----------이전 화면에서 넘어오는 이미지 값이 있다면 해당 값으로 이미지 수정
        currentPlantImage = PlantImage(R.drawable.delete_plant, "Default Image") // TODO: DB 연동 후 삭제
        val screenFrame = findViewById<FrameLayout>(R.id.screenFrame)

        val newPlantImageResId = intent.getIntExtra("newPlantImageResId", 0) // 다른 화면에서 전달되는 이미지
        if (newPlantImageResId != 0) { // 현재 이미지와 다른 이미지값이 들어온다면
            currentPlantImage = PlantImage(newPlantImageResId, "Custom Image")
            updateMainFlower(currentPlantImage.resourceId)
        }
        //----------신분증에서 식물 이미지 값에 따라 특정 화면 전환
        mainFlower.setOnClickListener {
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            when (currentPlantImage.resourceId) {
                R.drawable.delete_plant -> {
                    val fragment = PlantRegisterFragment()
                    /*fragmentTransaction.replace(R.id.screenFrame, fragment) // R.id.fragmentContainer는 Fragment를 표시할 레이아웃의 ID입니다.
                    fragmentTransaction.addToBackStack(null) // 백 스택에 추가 (선택 사항)
                    fragmentTransaction.commit()*/

                    fragmentTransaction.add(R.id.screenFrame, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                    screenFrame!!.bringToFront()

                }
                else -> {
                    val bundle = Bundle()
                    bundle.putParcelable("plantImage", mainFlower.drawable.toBitmap())
                    bundle.putString("plantName", plantName.text.toString())
                    bundle.putString("plantRace", plantRace)
                    bundle.putString("plantRegistration", plantRegistration)

                    val fragment = FlowerIdFragment(bundle)
                    fragmentTransaction.add(R.id.screenFrame, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                    screenFrame.bringToFront()

                }

            }

        }


        //------------------------센서
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout_main_health)

        linearLayout.setOnClickListener {
            // "LinearLayout" 클릭 시 SensorActivity로 이동
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
}
