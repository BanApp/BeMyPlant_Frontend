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

class DiaryNewFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary_new, container, false)
        return view
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        navController= Navigation.findNavController(view)

        val completeImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryNew_complete)
        completeImageButton.setOnClickListener(this)

        val photoImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryNew_photo)
        photoImageButton.setOnClickListener(this)


    }

    override fun onClick(v: View?){
        when(v?.id){
            R.id.imageButton_diaryNew_complete -> {
                val actionId = R.id.action_diaryNewFragment_to_diaryViewFragment
                navController.navigate(actionId)
            }
            R.id.imageButton_diaryNew_photo -> {
                val actionId = R.id.action_diaryNewFragment_to_diaryPhotoFragment
                navController.navigate(actionId)

            }

        }

    }

}