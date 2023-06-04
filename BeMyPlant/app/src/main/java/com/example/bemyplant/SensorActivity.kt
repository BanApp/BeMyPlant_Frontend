package com.example.plantver2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class SensorActivity : ComponentActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        val imageButton = findViewById<ImageButton>(R.id.imageButton_diaryView_complete)

        imageButton.setOnClickListener {
            // "LinearLayout" 클릭 시 SensorActivity로 이동
            val sensorIntent = Intent(this@SensorActivity, MainActivity::class.java)
            startActivity(sensorIntent)
        }
    }
}
