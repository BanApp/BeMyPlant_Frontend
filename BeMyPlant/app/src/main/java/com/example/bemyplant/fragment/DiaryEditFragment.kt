package com.example.bemyplant.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.bemyplant.Day
import com.example.bemyplant.R
import com.example.bemyplant.model.Diary
import com.example.bemyplant.model.DiaryRealmManager
import com.example.bemyplant.module.DiaryModule
import io.realm.Realm
import io.realm.RealmConfiguration

class DiaryEditFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController
    lateinit var weatherSpinner : Spinner
    lateinit var formattedDate: String
    var weatherCode : Int = 0

    lateinit var diaryImage: ImageView
    lateinit var diaryDateTextView: TextView
    lateinit var contentEditText: EditText
    lateinit var diaryTitleEditText: EditText
    lateinit var diaryRealmManager: DiaryRealmManager

    lateinit var selectedDay: Day
    lateinit var selectedDiaryImage: Bitmap
    var selectedWeatherCode: Int = 0
    lateinit var selectedContent: String
    lateinit var selectedDiaryTitle: String
    lateinit var weatherArray: Array<String>
    lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary_edit, container, false)
        //val selectedDay: Day? = arguments?.getParcelable("selectedDay")
        return view
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        diaryDateTextView = view.findViewById(R.id.textView_diaryEdit_day)
        diaryTitleEditText= view.findViewById(R.id.editText_diaryEdit_diaryTitle)
        diaryImage = view.findViewById(R.id.imageView_diaryEdit_plant)
        weatherSpinner = view.findViewById(R.id.spinner_diaryEdit_weather)
        contentEditText = view.findViewById(R.id.editText_diaryEdit_diaryContent)
        val configDiary : RealmConfiguration = RealmConfiguration.Builder()
            .name("diary.realm") // 생성할 realm 파일 이름 지정
            .deleteRealmIfMigrationNeeded()
            .modules(DiaryModule())
            .allowWritesOnUiThread(true) // sdhan : UI thread에서 realm에 접근할수 있게 허용
            .build()
        realm = Realm.getInstance(configDiary)
        diaryRealmManager = DiaryRealmManager(realm)
        weatherArray = resources.getStringArray(R.array.spinner_array)
        val bundle = arguments


        if (bundle != null && bundle.containsKey("selectedDay") && bundle.containsKey("title") && bundle.containsKey(
                "image"
            ) && bundle.containsKey("contents")
        ) {
            // TODO: 예외처리
            selectedDay = bundle.getParcelable<Day>("selectedDay")!!
            selectedDiaryImage = bundle.getParcelable("image")!!
            selectedWeatherCode = bundle.getInt("weatherCode")
            selectedContent = bundle.getString("contents")!!
            selectedDiaryTitle = bundle.getString("title")!!
            formattedDate = String.format(
                "%04d/%02d/%02d", selectedDay?.year, selectedDay?.month, selectedDay?.day
            )
            // (1) 화면 구성 (날짜)
            diaryDateTextView.text = formattedDate

            // (2) 화면 구성 (제목)
            diaryTitleEditText.setText(selectedDiaryTitle)

            // (3) 화면 구성 (이미지)
            diaryImage.setImageBitmap(selectedDiaryImage)

            // (4) 화면 구성 (날씨)
            weatherSpinner.setSelection(selectedWeatherCode)

            // (5) 화면 구성 (다이어리 내용)
            contentEditText.setText(selectedContent)
            Log.d("diary", "diary edit: 이전 화면으로부터 정보 받아와 렌더링 완료")
        }


        // selectedDay만 넘어오면 db조회
        if (bundle != null && bundle.containsKey("selectedDay")) {
            selectedDay = bundle.getParcelable<Day>("selectedDay")!!
            formattedDate = String.format(
                "%04d/%02d/%02d", selectedDay?.year, selectedDay?.month, selectedDay?.day
            )
            // 클릭된 셀의 Day 정보(날짜 정보)를 화면에 작성
            // 다이어리 DB 조회
            val diaryData = diaryRealmManager.find(formattedDate)

            if (diaryData == null) { // db에서 가져온 데이터가 null이라면 CalendarFragment로 이동
                // TODO: 예외처리 문구 (toast message, ...) "해당 날짜의 다이어리를 찾을 수 없습니다.."
                navController.navigate(R.id.calendarFragment)
            }

            // (1) 화면 구성 (날짜)
            diaryDateTextView.text = formattedDate

            // (2) 화면 구성 (제목)
            if (diaryData!!.Title.isNullOrEmpty()) {
                // TODO: 예외처리 문구 (toast message, ...) "해당 날짜의 다이어리 제목을 찾을 수 없습니다.."
                // navController.navigate(R.id.calendarFragment)
                diaryTitleEditText.setText("")
            }
            diaryTitleEditText.setText(diaryData.Title)

            // (3) 화면 구성 (이미지)
            if (diaryData!!.Image == null) {
                // TODO: 예외처리 문구 (toast message, ...) "해당 날짜의 사진을 찾을 수 없습니다.."
                diaryImage.setImageBitmap(null)
                // navController.navigate(R.id.calendarFragment)
            }
            // db의 bitarrary -> bitmap
            diaryImage.setImageBitmap(BitmapFactory.decodeByteArray(diaryData!!.Image, 0, diaryData!!.Image!!.size))

            // (4) 화면 구성 (날씨)
            if (diaryData!!.WeatherCode >= 0 && diaryData!!.WeatherCode < weatherArray.size) {
                weatherSpinner.setSelection(diaryData!!.WeatherCode)
            } else {
                // 유효하지 않은 인덱스 처리
                // TODO: 예외처리 문구 (toast message, ...) "해당 날짜의 날씨 정보를 찾을 수 없습니다.."
                // navController.navigate(R.id.calendarFragment)
                weatherSpinner.setSelection(0) //기본값 맑음으로 지정
            }

            // (5) 화면 구성 (다이어리 내용)
            if (diaryData!!.Content.isNullOrEmpty()) {
                // TODO: 예외처리 문구 (toast message, ...) "해당 날짜의 다이어리 내용을 찾을 수 없습니다.."
                // navController.navigate(R.id.calendarFragment)
                contentEditText.setText("")
            }
            contentEditText.setText(diaryData.Content)
            Log.d("diary", "diary edit: db조회 후 렌더링 완료")
        }


        // 날씨 (스피너) 클릭 처리
        weatherSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {weatherCode = position} //선택된 아이템 번호로 갱신

            override fun onNothingSelected(parent: AdapterView<*>?) {
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?){
        when(v?.id){
            R.id.imageButton_diaryEdit_photo -> {
                openGallery()
            }
            R.id.imageButton_diaryEdit_complete -> {
                // 다이어리 DB 업데이트
                val updateData = Diary()
                updateData.Date = formattedDate
                updateData.Title =  diaryTitleEditText.text.toString()
                updateData.WeatherCode = weatherCode
                updateData.Content = contentEditText.text.toString()
                updateData.Image = diaryImage.drawable.toBitmap().toByteArray() // bitmap -> byteArray (db)

                diaryRealmManager.update(formattedDate, updateData)

                val bundle = Bundle()
                bundle.putParcelable("selectedDay", selectedDay)
                bundle.putString("title", diaryTitleEditText.text.toString())
                bundle.putParcelable("image", diaryImage.drawable.toBitmap())
                bundle.putString("contents", contentEditText.text.toString())
                bundle.putInt("weatherCode", weatherCode)
                //bundle.putParcelable("selectedDay", selectedDay)
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