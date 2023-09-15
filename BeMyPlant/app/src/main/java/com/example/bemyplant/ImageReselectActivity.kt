package com.example.bemyplant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ImageReselectActivity: ComponentActivity() {
    private lateinit var finishButton: Button
    private lateinit var selectedImage1: ImageButton
    private lateinit var selectedImage2: ImageButton
    var selectedImage: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_reselect)


        // 이미지 선택하면 문자열이 넘어감.
        selectedImage1 = findViewById(R.id.imageButton_imageReselect_plant1)
        selectedImage1.setOnClickListener {
            selectedImage = "flower1"
            val intent = Intent(this, MainActivity::class.java)
            navigateToMain()
        }

        selectedImage2 = findViewById(R.id.imageButton_imageReselect_plant2)
        selectedImage2.setOnClickListener {
            selectedImage = "flower2"
            val intent = Intent(this, MainActivity::class.java)
            navigateToMain()
        }

        //finishButton = findViewById(R.id.button_imageReselect_finishButton)

        /*finishButton.setOnClickListener{
            // TODO: 식물 DB에 이미지 업데이트

            // 메인 화면으로 이동
            val homeIntent = Intent(this@ImageReselectActivity, MainActivity::class.java)
            startActivity(homeIntent)
        }*/
    }

    private fun navigateToMain(){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("selectedImage", selectedImage)
        startActivity(intent)
    }
}