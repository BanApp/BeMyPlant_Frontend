package com.example.bemyplant

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bemyplant.fragment.DeletePlantPopupFragment
import com.example.bemyplant.fragment.PushAlarmFragment
import com.example.bemyplant.fragment.WithdrawlPopupFragment

class SettingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation_main_menu)

        bottomNavigationView.selectedItemId = R.id.menu_setting
        val menuView = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
//        val selectedItemIndex = 3
//
//        menuView.getChildAt(selectedItemIndex).performClick()

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    // "홈" 메뉴 클릭 시 MainActivity로 이동
                    val homeIntent = Intent(this@SettingActivity, MainActivity::class.java)
                    startActivity(homeIntent)
                    true
                }

                R.id.menu_setting -> {
                    // "setting" 메뉴 클릭 시 SettingActivity로 이동
                    val boardIntent = Intent(this@SettingActivity, SettingActivity::class.java)
                    startActivity(boardIntent)
                    true
                }

                R.id.menu_chat -> {
                    // "채팅" 메뉴 클릭 시 ChatActivity로 이동
                    val chatIntent = Intent(this@SettingActivity, ChatActivity::class.java)
                    startActivity(chatIntent)
                    true
                }

                R.id.menu_diary -> {
                    // "일기" 메뉴 클릭 시 DiaryActivity로 이동
                    val diaryIntent = Intent(this@SettingActivity, DiaryActivity::class.java)
                    startActivity(diaryIntent)
                    true
                }

                else -> false
            }
        }

        val withdrawlButton = findViewById<Button>(R.id.withdrawalButton)
        val deleteButton = findViewById<Button>(R.id.deleteButton)
        val alarmButton=findViewById<Button>(R.id.alarmButton)
        val popupFrame = findViewById<FrameLayout>(R.id.popupFrame)

        //---회원탈퇴
        withdrawlButton.setOnClickListener{
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            val fragment: Fragment = WithdrawlPopupFragment()
            fragmentTransaction.add(R.id.popupFrame, fragment)

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            popupFrame.bringToFront()

        }
        //---삭제버튼
        deleteButton.setOnClickListener{
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            val fragment: Fragment = DeletePlantPopupFragment()
            fragmentTransaction.add(R.id.popupFrame, fragment)

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            popupFrame.bringToFront()

        }
        //----푸시알림버튼
        alarmButton.setOnClickListener{
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            val fragment: Fragment = PushAlarmFragment()
            fragmentTransaction.add(R.id.popupFrame, fragment)

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            popupFrame.bringToFront()

        }

    }
}
