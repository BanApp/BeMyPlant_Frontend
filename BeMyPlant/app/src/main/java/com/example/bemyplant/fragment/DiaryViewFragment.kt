package com.example.bemyplant.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bemyplant.CalendarAdapter
import com.example.bemyplant.Day
import com.example.bemyplant.DiaryActivity
import com.example.bemyplant.MainActivity
import com.example.bemyplant.R
import com.example.bemyplant.SettingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DiaryViewFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary_view, container, false)
        return view
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        navController= Navigation.findNavController(view)

        val bundle = arguments
        if (bundle != null && bundle.containsKey("selectedDay")) {
            val selectedDay = bundle.getParcelable<Day>("selectedDay")

            val formattedDate = String.format(
                "%04d/%02d/%02d", selectedDay?.year, selectedDay?.month, selectedDay?.day
            )

            // 클릭된 셀의 Day 정보(날짜 정보)를 화면에 작성
            val diaryDateTextView: TextView = view.findViewById(R.id.textView_diaryView_day)
            diaryDateTextView.text = formattedDate
        }



        val completeImageView: ImageView = view.findViewById(R.id.imageButton_diaryView_complete)
        completeImageView.setOnClickListener(this)

        val editImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryView_edit)
        editImageButton.setOnClickListener(this)

        val backImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryView_back)
        backImageButton.setOnClickListener(this)

    }

    override fun onClick(v: View?){
        when(v?.id){
            R.id.imageButton_diaryView_complete -> {
                val actionId = R.id.action_diaryViewFragment_to_calendarFragment
                navController.navigate(actionId)
            }
            R.id.imageButton_diaryView_edit -> {
                val actionId = R.id.action_diaryViewFragment_to_diaryEditFragment
                navController.navigate(actionId)

            }
            R.id.imageButton_diaryView_back -> {
                val actionId = R.id.action_diaryViewFragment_to_calendarFragment
                navController.navigate(actionId)

            }
        }

    }

}