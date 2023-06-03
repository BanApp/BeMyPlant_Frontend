import com.example.bemyplant.R
import com.example.bemyplant.ChatActivity
import com.example.bemyplant.DiaryActivity
import com.example.bemyplant.SettingActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivitytemp : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_temp)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.menu_bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.menu_home

        val menuView = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
//        val selectedItemIndex = 1
//
//        menuView.getChildAt(selectedItemIndex).performClick()

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.menu_home -> {
                    print("들어옴")
                    // "홈" 메뉴 클릭 시 MainActivity로 이동
                    val homeIntent = Intent(this@MainActivitytemp, MainActivity::class.java)
                    startActivity(homeIntent)
                    true
                }

                R.id.menu_setting -> {
                    print("들어옴")
                    // "setting" 메뉴 클릭 시 SettingActivity로 이동
                    val boardIntent = Intent(this@MainActivitytemp, SettingActivity::class.java)
                    startActivity(boardIntent)
                    true
                }

                R.id.menu_chat -> {
                    print("들어옴")
                    // "채팅" 메뉴 클릭 시 ChatActivity로 이동
                    val chatIntent = Intent(this@MainActivitytemp, ChatActivity::class.java)
                    startActivity(chatIntent)
                    true
                }

                R.id.menu_diary -> {
                    print("들어옴")
                    // "일기" 메뉴 클릭 시 DiaryActivity로 이동
                    val diaryIntent = Intent(this@MainActivitytemp, DiaryActivity::class.java)
                    startActivity(diaryIntent)
                    true
                }

                else -> false
            }
        }


    }
}
