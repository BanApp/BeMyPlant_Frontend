package com.example.bemyplant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.bemyplant.adapter.CalendarAdapter

class DiaryActivity : AppCompatActivity(){//, CalendarAdapter.ItemClickListener {

    lateinit var navController: NavController

    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)


    }
}
