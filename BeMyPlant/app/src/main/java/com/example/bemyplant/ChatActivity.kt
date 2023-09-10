package com.example.bemyplant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.example.bemyplant.data.ChatMsg
import com.example.bemyplant.adapter.MessageAdapter
class ChatActivity : AppCompatActivity() {
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var messageAdapterMe: MessageAdapter
    private lateinit var messageAdapterOther: MessageAdapter
    private val itemListMe = ArrayList<ChatMsg>()
    private val itemListOther = ArrayList<ChatMsg>()
    private val currentUser = "jo" //임시값
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        // 현재시간
        fun getTime():String{
            val now = System.currentTimeMillis()
            val date = Date(now)
            val dateFormat = SimpleDateFormat("hh:mm",Locale.getDefault())
            return dateFormat.format(date)


        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation_main_menu)

        bottomNavigationView.selectedItemId = R.id.menu_chat
        val menuView = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
//        val selectedItemIndex = 0
//
//        menuView.getChildAt(selectedItemIndex).performClick()

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
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

        messageAdapterMe = MessageAdapter(itemListMe, currentUser)
        messageAdapterOther = MessageAdapter(itemListOther, currentUser)

        recyclerViewMe.adapter = messageAdapterMe
        recyclerViewOther.adapter = messageAdapterOther

        recyclerViewMe.layoutManager = LinearLayoutManager(this)
        recyclerViewOther.layoutManager = LinearLayoutManager(this)

        sendButton.setOnClickListener {
            val message = messageEditText.text.toString()
            if (message.isNotEmpty()) {
                if (currentUser == "jo") {
                    itemListMe.add(ChatMsg(message, currentUser, getTime()))
                    messageAdapterMe.notifyDataSetChanged()
                    recyclerViewMe.scrollToPosition(itemListMe.size - 1)
                } else {
                    itemListOther.add(ChatMsg(message, "otherUserId", getTime()))
                    messageAdapterOther.notifyDataSetChanged()
                    recyclerViewOther.scrollToPosition(itemListOther.size - 1)
                }
                messageEditText.text.clear()
            }
        }
    }
}
