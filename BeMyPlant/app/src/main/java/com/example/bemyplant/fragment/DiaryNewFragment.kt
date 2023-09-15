package com.example.bemyplant.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.bemyplant.Day
import com.example.bemyplant.R


class DiaryNewFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController
    private lateinit var imagePicker: ActivityResultLauncher<Intent>
    private lateinit var spinner : Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary_new, container, false)
        imagePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val selectedImageUri: Uri? = data.data
                    // 선택한 이미지 URI를 사용하여 작업 수행
                    view?.findViewById<ImageView?>(R.id.imageView_diaryNew_plant)
                        ?.setImageURI(selectedImageUri)
                }
            }
        }
        return view
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle != null && bundle.containsKey("selectedDay")) {
            val selectedDay = bundle.getParcelable<Day>("selectedDay")

            val formattedDate = String.format(
                "%04d/%02d/%02d", selectedDay?.year, selectedDay?.month, selectedDay?.day
            )

            // 클릭된 셀의 Day 정보(날짜 정보)를 화면에 작성
            val diaryDateTextView: TextView = view.findViewById(R.id.TextView_diaryNew_day)
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

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        navController= Navigation.findNavController(view)

        val completeImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryNew_complete)
        completeImageButton.setOnClickListener(this)

        val photoImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryNew_photo)
        photoImageButton.setOnClickListener(this)


    }private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?){
        when(v?.id){
            R.id.imageButton_diaryNew_complete -> {
                val titleComponent: EditText? = view?.findViewById(R.id.editText_diaryNew_diaryTitle)
                val title = titleComponent.toString()

                val contentsComponent : EditText? = view?.findViewById(R.id.editText_diaryNew_diaryContent)
                val contents = contentsComponent.toString()


                if (title.isEmpty()){
                    showToast(requireContext(),"제목을 입력해주세요.")
                }
                if (contents.isEmpty()){
                    showToast(requireContext(),"내용을 입력해주세요.")
                }
                else{
                    val selectedDay = arguments?.getParcelable<Day>("selectedDay")

                    val bundle = Bundle()
                    bundle.putParcelable("selectedDay", selectedDay)
                    // TODO: (정현) 다이어리 DB 업데이트

                    // 날씨:
                    //val spinner: Spinner = view.findViewById(R.id.Spinner_diaryNew_weather)
                    //val selectedWeather = spinner.selectedItem.toString()

                    navController.navigate(R.id.diaryViewFragment, bundle)
                }


            }
            R.id.imageButton_diaryNew_photo -> {
                openGallery()
            }
        }
    }
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
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
                view?.findViewById<ImageView?>(R.id.imageView_diaryNew_plant)?.let {
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
        const val GALLERY_REQUEST_CODE = 101 // 앨범에서 사진을 가져오기 위한 요청 코드
    }



}