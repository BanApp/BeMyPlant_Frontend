package com.example.bemyplant.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bemyplant.CalendarAdapter
import com.example.bemyplant.Day
import com.example.bemyplant.DiaryActivity
import com.example.bemyplant.MainActivity
import com.example.bemyplant.R
import com.example.bemyplant.SettingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DiaryViewFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController
    lateinit var plantImage: ImageView
    lateinit var selectedDay: Day

    // 더미데이터 - 가상의 이미지 ID 맵 생성 (추후 실제 DB 값으로 대체)
    private val dateImageMap = hashMapOf(
        "2023-07-01" to R.drawable.example_photo,
        "2023-07-10" to R.drawable.flower,
    )

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
            selectedDay = bundle.getParcelable<Day>("selectedDay")!!

            val formattedDate = String.format(
                "%04d/%02d/%02d", selectedDay?.year, selectedDay?.month, selectedDay?.day
            )

            // 클릭된 셀의 Day 정보(날짜 정보)를 화면에 작성
            // TODO: (정현) 다이어리 DB 조회
            val diaryDateTextView: TextView = view.findViewById(R.id.textView_diaryView_day)
            diaryDateTextView.text = formattedDate

            // 이미지 -> db에 있으면 넣을 것
            plantImage = view.findViewById(R.id.imageView_diaryView_plant)
            val date =
                "${selectedDay?.year}-${String.format("%02d", selectedDay?.month)}-${String.format("%02d", selectedDay?.day)}"
            val hasImage = dateImageMap.containsKey(date)
            if (hasImage){
                plantImage.setImageResource(R.drawable.example_photo)
            }
            // TODO: 날씨 정보 넣을 것 (TextView)
        }

        val deleteImageButton: ImageView = view.findViewById(R.id.imageButton_diaryView_delete)
        deleteImageButton.setOnClickListener(this)

        val completeImageView: ImageView = view.findViewById(R.id.imageButton_diaryView_complete)
        completeImageView.setOnClickListener(this)

        val editImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryView_edit)
        editImageButton.setOnClickListener(this)

        val backImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryView_back)
        backImageButton.setOnClickListener(this)

    }

    override fun onClick(v: View?){
        when(v?.id){
            R.id.imageButton_diaryView_delete -> {
                showPopup()
            }

            R.id.imageButton_diaryView_complete -> {
                val actionId = R.id.action_diaryViewFragment_to_calendarFragment
                navController.navigate(actionId)
            }
            R.id.imageButton_diaryView_edit -> {
                val bundle = Bundle()
                bundle.putParcelable("selectedDay", selectedDay)

                //val actionId = R.id.action_diaryNewFragment_to_diaryViewFragment
                //var action = DiaryNewFragmentDirections.actionDiaryNewFragmentToDiaryViewFragment(selectedDate)
                navController.navigate(R.id.diaryEditFragment, bundle)

            }
            R.id.imageButton_diaryView_back -> {
                val actionId = R.id.action_diaryViewFragment_to_calendarFragment
                navController.navigate(actionId)

            }
        }

    }
    fun showPopup() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_diary_remove_popup, null)

        // 팝업 창의 뷰로 사용할 XML 레이아웃 설정
        builder.setView(dialogView)

        val dialog = builder.create()

        // 예 버튼 클릭 시의 동작
        dialogView.findViewById<AppCompatButton>(R.id.appCompatButton_diary_yes).setOnClickListener {
            // TODO: (정현) 다이어리 DB 삭제
            val actionId = R.id.action_diaryViewFragment_to_calendarFragment
            navController.navigate(actionId)
            dialog.dismiss()
        }

        // 아니오 버튼 클릭 시의 동작
        dialogView.findViewById<AppCompatButton>(R.id.appCompatButton_diary_no).setOnClickListener {
            dialog.dismiss() // 다이얼로그를 닫습니다.
        }

        dialog.show()

    }

}