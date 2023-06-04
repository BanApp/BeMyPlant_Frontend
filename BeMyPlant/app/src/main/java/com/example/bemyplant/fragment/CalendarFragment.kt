package com.example.bemyplant.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bemyplant.CalendarAdapter
import com.example.bemyplant.ChatActivity
import com.example.bemyplant.Day
import com.example.bemyplant.DiaryActivity
import com.example.bemyplant.MainActivity
import com.example.bemyplant.R
import com.example.bemyplant.SettingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CalendarFragment : Fragment(), CalendarAdapter.ItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        val dayList: ArrayList<Day> = getCalendarData()/*
        calendarAdapter = CalendarAdapter(requireContext(), dayList)
        calendarAdapter.setClickListener(this)*/

        calendarAdapter = CalendarAdapter(requireContext(), dayList, this) // itemClickListener 전달
        recyclerView.adapter = calendarAdapter


        // BottomNavigationView 설정
        val bottomNavigationView =
            view.findViewById<BottomNavigationView>(R.id.bottomNavigation_main_menu)
        bottomNavigationView.selectedItemId = R.id.menu_diary
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    val homeIntent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(homeIntent)
                    true
                }

                R.id.menu_setting -> {
                    val boardIntent = Intent(requireContext(), SettingActivity::class.java)
                    startActivity(boardIntent)
                    true
                }

                R.id.menu_chat -> {
                    val chatIntent = Intent(requireContext(), ChatActivity::class.java)
                    startActivity(chatIntent)
                    true
                }

                R.id.menu_diary -> {
                    // 현재 페이지이므로 아무 작업도 수행하지 않음
                    true
                }

                else -> false
            }
        }

        return view

    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        navController= Navigation.findNavController(view)
    }

    private fun getCalendarData(): ArrayList<Day> {
        val dayList = ArrayList<Day>()
        for (i in 1..30) {
            val day = Day()
            day.day = i.toString()
            day.isInMonth = true
            dayList.add(day)
        }
        return dayList
    }

    override fun onItemClick(view: View?, day: String?, isInMonth: Boolean) {
        // 날짜 클릭 시 다이어리 확인 or 작성 (구분하는 로직은 추후 구현 예정)
        if (isInMonth) {
            //Toast.makeText(requireContext(), "다이어리 작성 창 열기 - $day", Toast.LENGTH_SHORT).show()
            navController.navigate(R.id.diaryViewFragment)
        }
    }

}
