package com.example.bemyplant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ImageReselectActivity: ComponentActivity() {
    private lateinit var finishButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_reselect)

        finishButton = findViewById(R.id.button_imageReselect_finishButton)

        finishButton.setOnClickListener{
            // TODO: 식물 DB에 이미지 업데이트

            // 메인 화면으로 이동
            val homeIntent = Intent(this@ImageReselectActivity, MainActivity::class.java)
            startActivity(homeIntent)
        }
    }
}