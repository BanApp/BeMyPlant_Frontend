package com.example.plantver2.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plantver2.CalendarAdapter
import com.example.plantver2.Day
import com.example.plantver2.DiaryActivity
import com.example.plantver2.MainActivity
import com.example.plantver2.R
import com.example.plantver2.SettingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DiaryPhotoFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary_photo, container, false)
        return view
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        navController= Navigation.findNavController(view)

        val completeImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryPhoto_complete)
        completeImageButton.setOnClickListener(this)

    }

    override fun onClick(v: View?){
        when(v?.id){
            R.id.imageButton_diaryPhoto_complete -> {
                //backword .. 바로 전 화면으로 가야 함 (로직은 추후 구현 예정)
                val actionId = R.id.action_diaryPhotoFragment_to_diaryEditFragment
                navController.navigate(actionId)
            }

        }

    }

}