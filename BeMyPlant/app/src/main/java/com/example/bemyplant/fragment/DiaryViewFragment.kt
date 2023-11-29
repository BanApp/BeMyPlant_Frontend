package com.example.bemyplant.fragment

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.bemyplant.Day
import com.example.bemyplant.R
import com.example.bemyplant.model.DiaryRealmManager
import com.example.bemyplant.module.DiaryModule
import io.realm.Realm
import io.realm.RealmConfiguration


class DiaryViewFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController
    lateinit var formattedDate: String

    lateinit var diaryImage: ImageView
    lateinit var diaryDateTextView: TextView
    lateinit var weatherTextView: TextView
    lateinit var contentTextView: TextView
    lateinit var diaryTitleTextView: TextView
    private lateinit var diaryRealmManager: DiaryRealmManager

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
        val view = inflater.inflate(R.layout.fragment_diary_view, container, false)
        Log.d("diary", "diary view: onCreateView완료")
        return view
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("diary", "diary view: onViewCreated 시작")
        navController = Navigation.findNavController(view)
        diaryDateTextView = view.findViewById(R.id.textView_diaryView_day)
        diaryTitleTextView = view.findViewById(R.id.textView_diaryView_diaryTitle)
        diaryImage = view.findViewById(R.id.imageView_diaryView_plant)
        weatherTextView = view.findViewById(R.id.textView_diaryView_weather)
        contentTextView = view.findViewById(R.id.textView_diaryView_diaryContent)
        val configDiary : RealmConfiguration = RealmConfiguration.Builder()
            .name("diarydb.realm") // 생성할 realm 파일 이름 지정
            .deleteRealmIfMigrationNeeded()
            .modules(DiaryModule())
            .allowWritesOnUiThread(true) // sdhan : UI thread에서 realm에 접근할수 있게 허용
            .build()
        realm = Realm.getInstance(configDiary)
        diaryRealmManager = DiaryRealmManager(realm)
        weatherArray = resources.getStringArray(R.array.spinner_array)
        val bundle = arguments

        // selectedDay 를 비롯해 다이어리 내용, 날씨, 제목이 모두 넘어오면 db조회 없이 처리
        if (bundle != null && bundle.containsKey("selectedDay") && bundle.containsKey("title") && bundle.containsKey(
                "image"
            ) && bundle.containsKey("contege, selectedWeatherCode, selectedCnts")
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
            diaryTitleTextView.text = selectedDiaryTitle

            // (3) 화면 구성 (이미지)
            diaryImage.setImageBitmap(selectedDiaryImage)

            // (4) 화면 구성 (날씨)
            weatherTextView.text =  weatherArray[selectedWeatherCode]

            // (5) 화면 구성 (다이어리 내용)
            contentTextView.text = selectedContent
            Log.d("diary", "diary view: 이전 화면으로부터 정보 받아와 렌더링 완료")
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
                diaryTitleTextView.text = ""
            }
            diaryTitleTextView.text = diaryData.Title

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
                weatherTextView.text = weatherArray[diaryData!!.WeatherCode]
            } else {
                // 유효하지 않은 인덱스 처리
                // TODO: 예외처리 문구 (toast message, ...) "해당 날짜의 날씨 정보를 찾을 수 없습니다.."
                // navController.navigate(R.id.calendarFragment)
                weatherTextView.text = weatherArray[0] //기본값 맑음으로 지정
            }

            // (5) 화면 구성 (다이어리 내용)
            if (diaryData!!.Content.isNullOrEmpty()) {
                // TODO: 예외처리 문구 (toast message, ...) "해당 날짜의 다이어리 내용을 찾을 수 없습니다.."
                // navController.navigate(R.id.calendarFragment)
                contentTextView.text = ""
            }
            contentTextView.text = diaryData.Content
            Log.d("diary", "diary view: db 조회 후 렌더링 완료")

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
                showDeletePopup()
            }

            R.id.imageButton_diaryView_complete -> {
                val actionId = R.id.action_diaryViewFragment_to_calendarFragment
                navController.navigate(actionId)
            }
            R.id.imageButton_diaryView_edit -> {
                val bundle = Bundle()
                bundle.putParcelable("selectedDay", selectedDay)
                bundle.putString("title", diaryTitleTextView.text.toString())
                bundle.putParcelable("image", diaryImage.drawable.toBitmap())
                bundle.putString("contents", contentTextView.text.toString())
                bundle.putInt("weatherCode", selectedWeatherCode)

                navController.navigate(R.id.diaryEditFragment, bundle)

            }
            R.id.imageButton_diaryView_back -> {
                val actionId = R.id.action_diaryViewFragment_to_calendarFragment
                navController.navigate(actionId)
            }
        }

    }
    fun showDeletePopup() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_diary_remove_popup, null)

        // 팝업 창의 뷰로 사용할 XML 레이아웃 설정
        builder.setView(dialogView)

        val dialog = builder.create()

        // 예 버튼 클릭 시의 동작
        dialogView.findViewById<AppCompatButton>(R.id.appCompatButton_diary_yes).setOnClickListener {
            // 다이어리 DB에서 해당 날짜의 데이터 삭제
            diaryRealmManager.deleteByDate(formattedDate)
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