package com.example.bemyplant.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
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

class DiaryEditFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController
    lateinit var selectedDay: Day
    private lateinit var spinner : Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("[diaryEdit]", "diary edit start")
        val view = inflater.inflate(R.layout.fragment_diary_edit, container, false)
        //val selectedDay: Day? = arguments?.getParcelable("selectedDay")
        return view
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle != null && bundle.containsKey("selectedDay")) {
            selectedDay = bundle.getParcelable<Day>("selectedDay")!!

            val formattedDate = String.format(
                "%04d/%02d/%02d", selectedDay?.year, selectedDay?.month, selectedDay?.day
            )

            // 클릭된 셀의 Day 정보(날짜 정보)를 화면에 작성
            // TODO: (정현) 다이어리 DB 조회
            val diaryDateTextView: TextView = view.findViewById(R.id.textView_diaryEdit_day)
            diaryDateTextView.text = formattedDate
            

        }

        // 날씨 (스피너) 클릭 처리
        spinner = view.findViewById(R.id.Spinner_diaryNew_weather)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {}

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO: 날씨 정보 렌더링 (기본값 = 기존 날씨값)
            //val defaultItem =
                /*val defaultItem = "기본값" // 원하는 텍스트를 지정하세요
                val adapter = spinner.adapter
                if (adapter is ArrayAdapter<*>) {
                    adapter.add(defaultItem)
                }*/
            }
        }

        navController= Navigation.findNavController(view)

        val completeImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryEdit_complete)
        completeImageButton.setOnClickListener(this)

        val photoImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryEdit_photo)
        photoImageButton.setOnClickListener(this)


    }

    override fun onClick(v: View?){
        when(v?.id){
            R.id.imageButton_diaryEdit_photo -> {
                openGallery()
            }
            R.id.imageButton_diaryEdit_complete -> {
                // TODO: (정현) 다이어리 DB 업데이트
                // 날짜 정보도 같이 넘김
                val bundle = Bundle()
                bundle.putParcelable("selectedDay", selectedDay)
                navController.navigate(R.id.diaryViewFragment, bundle)
            }

        }

    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, DiaryNewFragment.GALLERY_REQUEST_CODE)
    }
    // 앨범에서 선택한 이미지를 처리하는 콜백
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedImageUri: Uri? = data.data
                // 선택한 이미지 URI를 사용하여 원하는 작업 수행
                // 예: 이미지 뷰에 이미지 표시
                // val imageView: ImageView = view?.findViewById(R.id.imageView)
                // Glide를 사용하여 이미지 로딩 및 크기 조절
                view?.findViewById<ImageView?>(R.id.imageView_diaryEdit_plant)?.let {
                    Glide.with(requireContext())
                        .load(selectedImageUri)
                        .override(600, 600) // 원하는 크기로 조절
                        .centerCrop() // 이미지를 중앙으로 맞춤
                        .into(it)
                }

                /*view?.findViewById<ImageView?>(R.id.imageView_diaryNew_plant)
                    ?.setImageURI(selectedImageUri)*/
            }
        }
    }


    companion object {
        private const val GALLERY_REQUEST_CODE = 101 // 앨범에서 사진을 가져오기 위한 요청 코드
    }

}