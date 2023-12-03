package com.example.bemyplant

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.bemyplant.fragment.PlantImageSelect1Fragment
import com.example.bemyplant.fragment.SensorRegisterFragment

class PlantRegisterForFragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_register_for_fragment)

        val username = intent.getStringExtra("username")
        println("♥♥♥♥♥♥♥♥♥♥♥♥♥♥")
        println(username)

        val bundle = bundleOf(
            "username" to username
        )

        val plantImageSelect1Fragment = PlantImageSelect1Fragment()
        plantImageSelect1Fragment.arguments = bundle
    }
}