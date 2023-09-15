package com.example.bemyplant

import retrofit2.Response
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.bemyplant.fragment.SettingDeleteAccountPopupFragment
import com.example.bemyplant.fragment.SettingDeletePlantPopupFragment
import com.example.bemyplant.fragment.SettingLogoutPopupFragment
import com.example.bemyplant.network.RetrofitService
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingActivity : AppCompatActivity() {
    private val retrofitService = RetrofitService().apiService

    private lateinit var userImage: ImageView
    private lateinit var realNameTextView: TextView
    private lateinit var uidTextView: TextView
    //private val pushAlarmButton: TextView = findViewById(R.id.textView_sensor_temperature)
    private lateinit var logoutButton: TextView
    private lateinit var deleteAccountButton: TextView
    private lateinit var deletePlantButton: TextView

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        userImage = findViewById(R.id.imageView_setting_user)
        realNameTextView = findViewById(R.id.textView_setting_name)
        uidTextView = findViewById(R.id.textView_setting_uid)
        logoutButton = findViewById(R.id.textView_setting_logout)
        deleteAccountButton = findViewById(R.id.textView_setting_deleteAccount)
        deletePlantButton = findViewById(R.id.textView_setting_deletePlant)
        // 계정 정보 API 호출 -> 계정, 실제 이름대로 uidTextView, nameTextView 수정
        getUserAccount()

        // TODO: 사용자 얼굴은 default값으로 미리 지정해둘 것
        // coroutine으로 처리할 것 (비동기)

        // TODO: 2. (정현) 식물 DB에서 식물 이름 가져옴 -> nameTextView 수정

        // (1) 로그아웃 버튼 클릭 시 처리
        logoutButton.setOnClickListener{
            logoutPopup() //팝업창 띄움
        }

        // (2) 회원탈퇴 버튼 클릭 시 처리
        deleteAccountButton.setOnClickListener{
            deleteAccountPopup() //팝업창 띄움
        }

        // (3) 식물삭제 버튼 클릭 시 처리
        deletePlantButton.setOnClickListener{
            deletePlantPopup() //팝업창 띄움

        }

        // 하단바
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
    }

    private fun getUserAccount() {
        val sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        val Tag: String = "sensor"
        //Log.d(Tag, "token: $token")

        // 토큰 부재 시 초기화면(MJ_main)으로 전환
        if (token.isNullOrEmpty()){
            val homeIntent = Intent(this@SettingActivity, MJ_MainActivity::class.java)
            startActivity(homeIntent)
        }
        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofitService.getUserData("Bearer " + token)
            val Tag: String = "sensor"
            Log.d(Tag, "sensor raw-response: $response")

            if (response.isSuccessful) {
                val UserData = response.body()

                runOnUiThread {
                    realNameTextView.text = response.body()?.r_name
                    uidTextView.text = response.body()?.username
                    // 화면 갱신
                    realNameTextView.invalidate()
                    uidTextView.invalidate()
                }
            }
        }
    }

    fun logoutPopup() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_setting_logout_popup, null)

        // 팝업 창의 뷰로 사용할 XML 레이아웃 설정
        builder.setView(dialogView)

        val dialog = builder.create()

        // 예 버튼 클릭 시의 동작
        dialogView.findViewById<AppCompatButton>(R.id.appCompatButton_logout_yes).setOnClickListener {
            // 로그아웃 (sharedProferences 삭제)
            val sharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            // "token" 데이터 삭제
            editor?.remove("token")

            // 변경 사항 적용
            editor?.apply()

            // 최초 화면으로 이동 (MJ_main)
            val homeIntent = Intent(this@SettingActivity, MJ_MainActivity::class.java)
            startActivity(homeIntent)
            dialog.dismiss()
        }

        // 아니오 버튼 클릭 시의 동작
        dialogView.findViewById<AppCompatButton>(R.id.appCompatButton_logout_no).setOnClickListener {
            dialog.dismiss() // 다이얼로그를 닫습니다.
        }

        dialog.show()

    }

    fun deleteAccountPopup(){
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_setting_delete_account_popup, null)

        // 팝업 창의 뷰로 사용할 XML 레이아웃 설정
        builder.setView(dialogView)

        val dialog = builder.create()

        // 예 버튼 클릭 시의 동작
        dialogView.findViewById<AppCompatButton>(R.id.appCompatButton_deleteAccount_yes).setOnClickListener {
            // 2. 회원 탈퇴 (rest API 로 계정 delete)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
                    val token = sharedPreferences.getString("token", null)

                    // 토큰 부재 시 초기화면(MJ_main)으로 전환
                    if (token.isNullOrEmpty()){
                        val homeIntent = Intent(this@SettingActivity, MJ_MainActivity::class.java)
                        startActivity(homeIntent)
                    }

                    // API 요청 보내기
                    val response = retrofitService.deleteAccount("Bearer " + token)
                    Log.d("setting", "token: ${token}")
                    Log.d("setting", "response: $response")
                    if (response.isSuccessful) {
                        // 회원 탈퇴 성공
                        withContext(Dispatchers.Main) {
                            showToast(this@SettingActivity, "회원탈퇴 되었습니다.")
                        }
                        // 최초 화면으로 이동 (MJ_main)
                        val homeIntent = Intent(this@SettingActivity, MJ_MainActivity::class.java)
                        startActivity(homeIntent)
                    } else {
                        // 회원탈퇴 실패
                        withContext(Dispatchers.Main) {
                            showToast(this@SettingActivity, "회원탈퇴 실패")
                        }
                    }
                } catch (e: Exception) {
                    // API 요청 실패
                    withContext(Dispatchers.Main) {
                        showToast(this@SettingActivity, "API 요청 실패: ${e.message}")
                    }
                }
            }
        }

        // 아니오 버튼 클릭 시의 동작
        dialogView.findViewById<AppCompatButton>(R.id.appCompatButton_deleteAccount_no).setOnClickListener {
            dialog.dismiss() // 다이얼로그를 닫습니다.
        }

        dialog.show()

    }

    fun deletePlantPopup(){
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_setting_delete_account_popup, null)

        // 팝업 창의 뷰로 사용할 XML 레이아웃 설정
        builder.setView(dialogView)

        val dialog = builder.create()

        // 예 버튼 클릭 시의 동작
        dialogView.findViewById<AppCompatButton>(R.id.appCompatButton_deleteAccount_yes).setOnClickListener {
            // 2. 식물 삭제
            // TODO: 3. (정현) 식물 DB에서 식물 삭제
            // 비동기로 처리 ? (고려중)

            // 3. 메인 화면으로 (식물 이미지 = default = +)
            val homeIntent = Intent(this@SettingActivity, MainActivity::class.java)
            startActivity(homeIntent)
        }

        // 아니오 버튼 클릭 시의 동작
        dialogView.findViewById<AppCompatButton>(R.id.appCompatButton_deleteAccount_no).setOnClickListener {
            dialog.dismiss() // 다이얼로그를 닫습니다.
        }

        dialog.show()
    }

}
