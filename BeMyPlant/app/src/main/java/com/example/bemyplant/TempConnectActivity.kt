package com.example.bemyplant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import com.example.bemyplant.fragment.PlantImageSelect1Fragment
import com.example.bemyplant.fragment.SensorRegisterFragment

class TempConnectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_connect)

        val username = intent.getStringExtra("username")
        println("♥♥♥♥♥♥♥♥♥♥♥♥♥♥")
        println(username)

        val bundle = bundleOf(
            "username" to username
        )
        val sensorRegisterFragment = SensorRegisterFragment()
        sensorRegisterFragment.arguments = bundle
    }
}
