package com.example.bemyplant

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bemyplant.adapter.MessageAdapter
import com.example.bemyplant.data.ChatMsg
import com.example.bemyplant.data.ChatRequest
import com.example.bemyplant.network.RetrofitService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ChatActivity : AppCompatActivity() {
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var messageAdapterMe: MessageAdapter
    private lateinit var messageAdapterOther: MessageAdapter

    private val itemListMe = ArrayList<ChatMsg>()
    private val itemListOther = ArrayList<ChatMsg>()
    private val currentUser = "jo" //임시값
    private val retrofitService = RetrofitService().apiService2
    private lateinit var defaultUserImage: Bitmap
    private lateinit var user_image: Bitmap
    private lateinit var plant_image: Bitmap

    private fun getTime(): String {
        val now = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
        return dateFormat.format(date)
    }

    private fun renderingMessage(message: String, user:String, recyclerViewMe: RecyclerView, recyclerViewOther: RecyclerView) {
        Log.d("chatbot", "[function] message: $message")
        Log.d("chatbot", "[function] user: $user")
        val transparentBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        transparentBitmap.eraseColor(Color.TRANSPARENT)

        if (message.isNotEmpty()) {
            if (user == "jo") {
                itemListMe.add(ChatMsg(message, currentUser, getTime(), user_image))
                messageAdapterMe.notifyDataSetChanged()
                recyclerViewMe.scrollToPosition(itemListMe.size - 1)

                // 빈 문자열 추가
                itemListOther.add(ChatMsg(" ".repeat(message.length), currentUser, "", transparentBitmap))
                // findViewById<ImageView>(R.id.plantImage).visibility = View.INVISIBLE
                messageAdapterOther.notifyDataSetChanged()
                recyclerViewOther.scrollToPosition(itemListMe.size - 1)
            } else {
                //findViewById<ImageView>(R.id.plantImage).visibility = View.VISIBLE
                Log.d("chatbot", "[function] other user enter")
                itemListOther.add(ChatMsg(message, user, getTime(), plant_image))
                Log.d("chatbot", "[function] (1) itemListOther.add")
                messageAdapterOther.notifyDataSetChanged()
                Log.d("chatbot", "[function] (2) messageAdapterOther")
                recyclerViewOther.scrollToPosition(itemListOther.size - 1)
                Log.d("chatbot", "[function] (3) recyclerViewOther")

                // 빈 문자열 추가
                itemListMe.add(ChatMsg(" ".repeat(message.length), currentUser, "", transparentBitmap))
                Log.d("chatbot", "[function] (4) itemListOther.add")
                messageAdapterMe.notifyDataSetChanged()
                Log.d("chatbot", "[function] (5) messageAdapterOtherjmj0801")
                recyclerViewMe.scrollToPosition(itemListMe.size - 1)
                Log.d("chatbot", "[function] (6) recyclerViewOther")
            }
            messageEditText.text.clear()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        lifecycleScope.launch {
            defaultUserImage = drawableResourceToBitmap(this@ChatActivity, R.drawable.user_image)!!
        }
        //user_image = defaultUserImage

        // 사용자 이미지 가져오기
        // TODO: (정현) 사용자 이미지 가져오기 (user_image에 넣으면 됨) -  bitmap임
        // TODO: (정현) 식물 이미지 가져오기 (plant_image 넣으면 됨) - bitmap임
        // user_image = bitmap
//        val storageDir: File? = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//        val filename = "BMEYPLANT_USER_IMAGE.jpg"
//        val file = File(storageDir, filename)
//        Log.d("이미지 read 경로", file.absolutePath)
//        val userImageFromFile = readBitmapFromExternalStorage(file)
//        if (userImageFromFile != null) {
//            user_image = userImageFromFile
//        }
//        else{
//            user_image = defaultUserImage
//        }


        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottomNavigation_main_menu)

        bottomNavigationView.selectedItemId = R.id.menu_chat
        //val menuView = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    // "홈" 메뉴 클릭 시 MainActivity로 이동
                    val homeIntent = Intent(this@ChatActivity, MainActivity::class.java)
                    startActivity(homeIntent)
                    true
                }

                R.id.menu_setting -> {
                    // "setting" 메뉴 클릭 시 SettingActivity로 이동
                    val boardIntent = Intent(this@ChatActivity, SettingActivity::class.java)
                    startActivity(boardIntent)
                    true
                }

                R.id.menu_chat -> {
                    // "채팅" 메뉴 클릭 시 ChatActivity로 이동
                    val chatIntent = Intent(this@ChatActivity, ChatActivity::class.java)
                    startActivity(chatIntent)
                    true
                }

                R.id.menu_diary -> {
                    // "일기" 메뉴 클릭 시 DiaryActivity로 이동
                    val diaryIntent = Intent(this@ChatActivity, DiaryActivity::class.java)
                    startActivity(diaryIntent)
                    true
                }

                else -> false
            }
        }

        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.sendButton)

        val recyclerViewMe = findViewById<RecyclerView>(R.id.recycler_view_me)
        val recyclerViewOther = findViewById<RecyclerView>(R.id.recycler_view_other)

        messageAdapterMe = MessageAdapter(itemListMe, currentUser, user_image)
        messageAdapterOther = MessageAdapter(itemListOther, "other", user_image)

        recyclerViewMe.adapter = messageAdapterMe
        recyclerViewOther.adapter = messageAdapterOther

        recyclerViewMe.layoutManager = LinearLayoutManager(this)
        recyclerViewOther.layoutManager = LinearLayoutManager(this)

        ///*
        sendButton.setOnClickListener {
            val message = messageEditText.text.toString()
            CoroutineScope(Dispatchers.Main).launch { // 메인 스레드에서 코루틴 실행
                renderingMessage(message, "jo", recyclerViewMe, recyclerViewOther)

                val chatData = ChatRequest(message)
                val sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
                val token: String? = sharedPreferences.getString("token", null)

                if (token != null) {
                    Log.d("full-token", "Bearer " + token)
                }

                // 토큰 부재 시 초기화면(MJ_main)으로 전환
                if (token.isNullOrEmpty()) {
                    val homeIntent = Intent(this@ChatActivity, MJ_MainActivity::class.java)
                    startActivity(homeIntent)
                }

                val response = retrofitService.chat(chatData, "Bearer " + token)

                if (response.code() == 401) {
                    // 401 Unauthorized 오류 처리
                    // 첫 화면으로 (로그인 시도 화면) 이동
                    val homeIntent = Intent(this@ChatActivity, MJ_MainActivity::class.java)
                    startActivity(homeIntent)
                } else {
                    // TODO: 채팅 데이터 api call - 다른 HTTP 오류 코드에 대한 처리
                }

                val Tag: String = "chatbot"
                Log.d(Tag, "chatbot raw-response: $response")
                Log.d(Tag, "chatbot raw-response-body: ${response.body()}")
                Log.d(Tag, "chatbot raw-response-body-response: ${response.body()?.response}")


                if (response.isSuccessful) {
                    val responseString = response.body()?.response
                    if (responseString != null) {
                        renderingMessage(responseString, "otherUser", recyclerViewMe, recyclerViewOther)
                    }
                }

            }
        }
    }


    fun readBitmapFromExternalStorage(file: File): Bitmap? {
        return try {
            BitmapFactory.decodeFile(file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    fun drawableResourceToBitmap(context: Context, drawableResId: Int): Bitmap? {
        return runBlocking {
            withContext(Dispatchers.IO) {
                // Drawable을 가져옵니다.
                val drawable = ContextCompat.getDrawable(context, drawableResId)

                // Drawable을 Bitmap으로 변환합니다.
                drawableToBitmap(drawable)
            }
        }
    }

    // 변환 함수
    fun drawableToBitmap(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}

