package com.example.bemyplant.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bemyplant.CalendarAdapter
import com.example.bemyplant.ChatActivity
import com.example.bemyplant.Day
import com.example.bemyplant.MainActivity
import com.example.bemyplant.R
import com.example.bemyplant.SettingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class CalendarFragment : Fragment(), CalendarAdapter.ItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    lateinit var navController: NavController

    // 더미데이터 - 가상의 이미지 ID 맵 생성 (추후 실제 DB 값으로 대체)
    private val dateImageMap = hashMapOf(
        "2023-07-01" to R.drawable.example_photo,
        "2023-07-10" to R.drawable.flower,
    )

    // 현재 날짜 정보
    @RequiresApi(Build.VERSION_CODES.O)
    private var currentDate: LocalDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 7)


        // 캘린더 설정
        val dayList: ArrayList<Day> = getCalendarData(currentDate)/*
        calendarAdapter = CalendarAdapter(requireContext(), dayList)
        calendarAdapter.setClickListener(this)*/
        calendarAdapter =
            CalendarAdapter(requireContext(), dayList, this, dateImageMap) // itemClickListener 전달
        recyclerView.adapter = calendarAdapter

        updateCalendar(view)

        // 이전 달 캘린더 조회 클릭 이벤트
        val prevMonthImageView = view.findViewById<ImageView>(R.id.prevMonthImageView)
        prevMonthImageView.setOnClickListener {
            // 현재 날짜를 이전 달로 설정
            currentDate = currentDate.minusMonths(1)
            updateCalendar(view)
        }

        // 다음 달 캘린더 조회 클릭 이벤트
        val nextMonthImageView = view.findViewById<ImageView>(R.id.nextMonthImageView)
        nextMonthImageView.setOnClickListener {
            // 현재 날짜를 이전 달로 설정
            currentDate = currentDate.plusMonths(1)
            updateCalendar(view)
        }

        // BottomNavigationView 설정
        val bottomNavigationView =
            view.findViewById<BottomNavigationView>(R.id.bottomNavigation_main_menu)
        bottomNavigationView.selectedItemId = R.id.menu_diary
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    val homeIntent = Intent(activity, MainActivity::class.java)
                    startActivity(homeIntent)
                    true
                }

                R.id.menu_setting -> {
                    val boardIntent = Intent(activity, SettingActivity::class.java)
                    startActivity(boardIntent)
                    true
                }

                R.id.menu_chat -> {
                    val chatIntent = Intent(activity, ChatActivity::class.java)
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

    // 캘린더 데이터 업데이트 메서드
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateCalendar(view: View) {
        val dayList: ArrayList<Day> = getCalendarData(currentDate)
        calendarAdapter.updateData(dayList)

        // 현재 년도와 월을 나타내는 TextView를 찾음
        val monthTextView = view.findViewById<TextView>(R.id.monthTextView)

        // 현재 날짜 정보 가져오기
        //val currentDate = LocalDate.now()

        // 년도와 월을 표시할 형식 지정 (예: "23년 7월")
        val formatter = DateTimeFormatter.ofPattern("yy년 M월", Locale.getDefault())

        // TextView에 현재 년도와 월을 표시
        monthTextView.text = currentDate.format(formatter)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCalendarData(current: LocalDate): ArrayList<Day> {
        val dayList = ArrayList<Day>()

        // 현재 날짜 정보 가져오기
        val currentDate = current
        val currentYear = currentDate.year
        val currentMonth = currentDate.monthValue

        // 현재 달의 첫 날 구하기
        val firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth())

        // 현재 달의 마지막 날 구하기
        val lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth())

        // 현재 달의 첫 날의 요일 구하기 (일요일: 7, 월요일: 1, ..., 토요일: 6)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value

        // 현재 달의 마지막 날짜
        val lastDay = lastDayOfMonth.dayOfMonth


        // 이전 달의 마지막 날 구하기
        val lastDayOfPrevMonth = firstDayOfMonth.minusDays(1)

        // 이전 달의 마지막 날짜
        val lastPrevDay = lastDayOfPrevMonth.dayOfMonth

        // 현재 달의 시작일(1일)에서 남은 요일만큼 이전 달 날짜로 채우기
        for (i in firstDayOfWeek - 1 downTo 0) {
            val day = Day()
            day.year =
                if (currentMonth == 1) currentYear - 1 else currentYear // 현재 달이 1월인 경우 연도를 줄여 이전 연도로 설정
            day.month = if (currentMonth == 1) 12 else currentMonth - 1 // 현재 달이 1월인 경우 이전 달은 12월
            day.day = lastPrevDay - i
            day.isInMonth = false
            dayList.add(day)
        }

        // 현재 달 날짜 추가
        for (i in 1..lastDay) {
            val day = Day()
            day.year = currentYear
            day.month = currentMonth
            day.day = i
            day.isInMonth = true
            dayList.add(day)
        }

        // 다음 달의 날짜로 채우기
        val remainingDays = 42 - dayList.size // 6주 x 7일
        for (i in 1..remainingDays) {
            val day = Day()
            day.year =
                if (currentMonth == 12) currentYear + 1 else currentYear // 현재 달이 12월인 경우 연도를 늘려 다음 연도로 설정
            day.month = if (currentMonth == 12) 1 else currentMonth + 1 // 현재 달이 12월인 경우 다음 달은 1월
            day.day = i
            day.isInMonth = false
            dayList.add(day)
        }


        return dayList

    }

    /*
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCalendarData(): ArrayList<Day> {
        val dayList = ArrayList<Day>()

        // 현재 날짜 정보 가져오기
        val currentDate = LocalDate.now()

        // 현재 달의 첫 날 구하기
        val firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth())

        // 현재 달의 마지막 날 구하기
        val lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth())

        // 현재 달의 첫 날의 요일 구하기 (일요일: 7, 월요일: 1, ..., 토요일: 6)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value

        // 현재 달의 마지막 날짜
        val lastDay = lastDayOfMonth.dayOfMonth


        // 이전 달의 마지막 날 구하기
        val lastDayOfPrevMonth = firstDayOfMonth.minusDays(1)

        // 이전 달의 마지막 날짜
        val lastPrevDay = lastDayOfPrevMonth.dayOfMonth

        // 현재 달의 시작일(1일)에서 남은 요일만큼 이전 달 날짜로 채우기
        for (i in firstDayOfWeek - 1 downTo 0) {
            val day = Day()
            day.day = (lastPrevDay - i).toString()
            day.isInMonth = false
            dayList.add(day)
        }

        // 현재 달 날짜 추가
        for (i in 1..lastDay) {
            val day = Day()
            day.day = i.toString()
            day.isInMonth = true
            dayList.add(day)
        }

        // 다음 달의 날짜로 채우기
        val remainingDays = 42 - dayList.size // 6주 x 7일
        for (i in 1..remainingDays) {
            val day = Day()
            day.day = i.toString()
            day.isInMonth = false
            dayList.add(day)
        }


        return dayList

    }
*/
    override fun onItemClick(view: View?, day: Day) {
        // 날짜 클릭 시 다이어리 확인 or 작성
        val date =
            "${day.year}-${String.format("%02d", day.month)}-${String.format("%02d", day.day)}"
        val hasImage = dateImageMap.containsKey(date)

        if (hasImage) {
            // 기존 다이어리 확인
            val bundle = Bundle()
            bundle.putParcelable("selectedDay", day)
            navController.navigate(R.id.diaryViewFragment, bundle)
        } else {
            // 새 다이어리 작성
            val bundle = Bundle()
            bundle.putParcelable("selectedDay", day)
            navController.navigate(R.id.diaryNewFragment, bundle)
        }
    }
}


