package com.example.bemyplant.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import java.io.ByteArrayOutputStream


class DiaryNewFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController
    private lateinit var imagePicker: ActivityResultLauncher<Intent>
    private lateinit var weatherSpinner : Spinner
    lateinit var formattedDate: String
    lateinit var selectedDay: Day
    var weatherCode : Int = 0

    lateinit var diaryImage: ImageView
    lateinit var diaryDateTextView: TextView
    lateinit var contentEditText: EditText
    lateinit var diaryTitleEditText: EditText
    lateinit var diaryRealmManager: DiaryRealmManager
    lateinit var realm: Realm

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

        navController = Navigation.findNavController(view)
        diaryDateTextView = view.findViewById(R.id.textView_diaryNew_day)
        diaryTitleEditText= view.findViewById(R.id.editText_diaryNew_diaryTitle)
        diaryImage = view.findViewById(R.id.imageView_diaryNew_plant)
        weatherSpinner = view.findViewById(R.id.spinner_diaryNew_weather)
        contentEditText = view.findViewById(R.id.editText_diaryNew_diaryContent)
        val configDiary : RealmConfiguration = RealmConfiguration.Builder()
            .name("diarydb.realm") // 생성할 realm 파일 이름 지정
            .deleteRealmIfMigrationNeeded()
            .modules(DiaryModule())
            .allowWritesOnUiThread(true) // sdhan : UI thread에서 realm에 접근할수 있게 허용
            .build()
        realm = Realm.getInstance(configDiary)
        diaryRealmManager = DiaryRealmManager(realm)
        val bundle = arguments
        if (bundle != null && bundle.containsKey("selectedDay")) {
            selectedDay = bundle.getParcelable<Day>("selectedDay")!!

            formattedDate = String.format(
                "%04d/%02d/%02d", selectedDay?.year, selectedDay?.month, selectedDay?.day
            )

            // 클릭된 셀의 Day 정보(날짜 정보)를 화면에 작성
            diaryDateTextView.text = formattedDate
        }
        else{
            // TODO: 예외처리 (toast message) "옳지 못합 접근입니다.."
            navController.navigate(R.id.calendarFragment)
        }


        // 날씨 (스피너) 클릭 처리
        weatherSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {weatherCode = position} //선택된 아이템 번호로 갱신

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        navController= Navigation.findNavController(view)

        val completeImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryNew_complete)
        completeImageButton.setOnClickListener(this)

        val photoImageButton: ImageButton = view.findViewById(R.id.imageButton_diaryNew_photo)
        photoImageButton.setOnClickListener(this)


    }
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?){
        when(v?.id){
            R.id.imageButton_diaryNew_complete -> {
                if (diaryTitleEditText.text==null || contentEditText.text == null || diaryImage.drawable == null ){
                    // TODO: 각 케이스별로 예외처리
                    showToast(requireContext(),"입력을 완료해주세요.")
                    return
                }
                val title = diaryTitleEditText.text.toString() //diaryTitleEditText.toString()
                val contents = contentEditText.text.toString()
                val image = diaryImage.drawable.toBitmap()
                if (image==null){
                    showToast(requireContext(),"사진을 입력해주세요.")
                    return
                }
                if (title.isEmpty()){
                    showToast(requireContext(),"제목을 입력해주세요.")
                    return
                }
                if (contents.isEmpty()){
                    showToast(requireContext(),"내용을 입력해주세요.")
                    return
                }

                else{
                    // DB 생성
                    val updateData = Diary()
                    updateData.Date = formattedDate
                    updateData.Title =  diaryTitleEditText.text.toString()
                    updateData.WeatherCode = weatherCode
                    updateData.Content = contentEditText.text.toString()
                    updateData.Image = diaryImage.drawable.toBitmap().toByteArray() // bitmap -> byteArray (db)

                    diaryRealmManager.create(updateData)
                    Log.d("diary", "diary new: db에 넣기 성공")

                    val bundle = Bundle()
                    bundle.putParcelable("selectedDay", selectedDay)
                    bundle.putString("title", diaryTitleEditText.text.toString())
                    bundle.putParcelable("image", diaryImage.drawable.toBitmap())
                    bundle.putString("contents", contentEditText.text.toString())
                    bundle.putInt("weatherCode", weatherCode)
                    //bundle.putParcelable("selectedDay", selectedDay)
                    Log.d("diary", "diary new: bundle 설정 성공")
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

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
