package com.example.bemyplant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import com.example.bemyplant.fragment.PlantImageSelect1Fragment

class PlantImageTempActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_image_temp)

        val mainIntent = getIntent()
        val username = mainIntent.getStringExtra("username")
        println("♥♥♥♥♥♥♥♥♥♥♥♥♥♥")
        println(username)

        val bundle = bundleOf(
            "username" to username
        )
        val plantImageSelect1Fragment = PlantImageSelect1Fragment()
        plantImageSelect1Fragment.arguments = bundle

    }
}