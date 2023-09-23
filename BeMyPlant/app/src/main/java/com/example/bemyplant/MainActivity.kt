package com.example.bemyplant

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.ImageButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.bemyplant.fragment.FlowerIdFragment
import com.example.bemyplant.fragment.PlantRegisterFragment

data class PlantImage(val resourceId: Int, val description: String)

class MainActivity : AppCompatActivity() {
    private lateinit var currentPlantImage: PlantImage

    fun updateMainFlower(newPlantImageResId: Int) {
        val mainFlower = findViewById<ImageButton>(R.id.mainFlower)
        mainFlower.setImageResource(newPlantImageResId)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //-----------식물 클릭시, 신분증 나오는 부분
        currentPlantImage = PlantImage(R.drawable.flower, "Default Image")
        val mainFlower = findViewById<ImageButton>(R.id.mainFlower)
        val screenFrame = findViewById<FrameLayout>(R.id.screenFrame)
        val newPlantImageResId = intent.getIntExtra("newPlantImageResId", 0)
        if (newPlantImageResId != 0) {
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
                    fragmentTransaction.add(R.id.screenFrame, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                    screenFrame.bringToFront()

                }
                else -> {
                    val fragment = FlowerIdFragment()
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
