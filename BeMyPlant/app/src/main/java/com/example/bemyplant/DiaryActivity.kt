package com.example.plantver2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class DiaryActivity : AppCompatActivity(){//, CalendarAdapter.ItemClickListener {

    lateinit var navController: NavController

    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        //navController = findNavController(R.id.nav_host_fragment)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }

    /*override fun onItemClick(view: View?, day: String?, isInMonth: Boolean) {
        TODO("Not yet implemented")
    }*/

}
