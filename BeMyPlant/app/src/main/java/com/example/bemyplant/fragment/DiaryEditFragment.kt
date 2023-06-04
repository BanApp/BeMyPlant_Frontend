package com.example.bemyplant.fragment

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
import com.example.bemyplant.CalendarAdapter
import com.example.bemyplant.Day
import com.example.bemyplant.DiaryActivity
import com.example.bemyplant.MainActivity
import com.example.bemyplant.R
import com.example.bemyplant.SettingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DiaryEditFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary_edit, container, false)
        return view
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        navController= Navigation.findNavController(view)

        val completeImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryEdit_complete)
        completeImageButton.setOnClickListener(this)

        val photoImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryEdit_photo)
        photoImageButton.setOnClickListener(this)


    }

    override fun onClick(v: View?){
        when(v?.id){
            R.id.imageButton_diaryEdit_photo -> {
                val actionId = R.id.action_diaryEditFragment_to_diaryPhotoFragment
                navController.navigate(actionId)
            }
            R.id.imageButton_diaryEdit_complete -> {
                val actionId = R.id.action_diaryEditFragment_to_diaryViewFragment
                navController.navigate(actionId)

            }

        }

    }

}