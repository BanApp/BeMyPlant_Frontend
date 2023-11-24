package com.example.bemyplant.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.bemyplant.MainActivity
import com.example.bemyplant.data.LoginData
import com.example.bemyplant.databinding.FragmentLoginBinding
import com.example.bemyplant.model.PlantModel
import com.example.bemyplant.module.PlantModule
import com.example.bemyplant.network.RetrofitService
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var binding: FragmentLoginBinding

class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    /*저장된 값 userId, userPw*/

    private lateinit var realm : Realm
//    lateinit var realm2 : Realm


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

//        Realm.init(requireContext())

        val configPlant : RealmConfiguration = RealmConfiguration.Builder()
            .name("appdb.realm") // 생성할 realm 파일 이름 지정
            .deleteRealmIfMigrationNeeded()
            .modules(PlantModule())
            .allowWritesOnUiThread(true) // sdhan : UI thread에서 realm에 접근할수 있게 허용
            .build()
        realm = Realm.getInstance(configPlant)
//        Realm.setDefaultConfiguration(configPlant)


//        val configDiary : RealmConfiguration = RealmConfiguration.Builder()
//            .name("dairydb.realm") // 생성할 realm 파일 이름 지정
//            .deleteRealmIfMigrationNeeded()
//            .schemaVersion(2)
//            .modules(DiaryModule())
//            .allowWritesOnUiThread(true) // sdhan : UI thread에서 realm에 접근할수 있게 허용
//            .build()
//        realm2 = Realm.getInstance(configDiary)
    }
    /*입력받은 값 -> api 인터페이스의 Login 호출 -> 아이디와 비밀번호 인자로 전달 -> 응답 처리*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.visibleButton.setOnClickListener {
            val currentTransformation = binding.userPwInput.transformationMethod
            if (currentTransformation is PasswordTransformationMethod) {
                binding.userPwInput.transformationMethod = SingleLineTransformationMethod.getInstance()
            } else {
                binding.userPwInput.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        var plantImageURL: String?
        plantImageURL = "https://i.namu.wiki/i/1kbHf_bcKUZ2Lzr9vJsKfu52TT_-10gfUI772kAG18xTs7Xx0Poko3GlUPH0kHpn49dzImSVf4PoKTJxhuQFzESahQEnOLUck3fIhaYVzQaOxZwoEeZYUakBl3TLzOOg33lIFLTZevkBAj-d0DlPcA.svg"

        var binaryData: ByteArray? = null
        binaryData = byteArrayOf(0x00,0x01,0x001)

//        loadImage(plantImageURL)
//
//        Thread {
//            loadImage(plantImageURL)
//            println(loadImage(plantImageURL))
//        }.start()

//        imageLoadFromURL(plantImageURL)

//        realm.executeTransaction{
//            with(it.createObject(PlantModel::class.java)){
//                this.P_Name = "Rose"
//                this.P_Birth = "2023-11-01"
//                this.P_Race = "Rosaceae"
//                this.P_Image = binaryData
//                this.P_Registration = "231101-7654321"
//            }
//        }

//        var plantImageBinary: ByteArray = imageLoadFromURL(plantImageURL)
//        var plantImageString: Bitmap = imageLoadFromURL(plantImageURL)
//        println("+++++++++++++++++++")
//        println("+++++++++++++++++++")
//        println(loadImage(plantImageURL))

        val vo = realm.where(PlantModel::class.java).equalTo("P_Name", "rose49").findFirst()

        if (vo != null) {
            println("+++++++++++++++++++")
            println(vo.P_Name)
            println("+++++++++++++++++++")
            println(vo.P_Birth)
            println("+++++++++++++++++++")
//            println(vo.P_Image)
            println("+++++++++++++++++++")
//            println(vo.P_Image.toString())
            println("+++++++++++++++++++")
            println(vo.P_Registration)
        }

        binding.startButton.setOnClickListener {
            val loginData = getLoginData()

            if (loginData.username.isEmpty()){
                showToast(requireContext(),"아이디를 입력하세요")
            }else if (loginData.password.isEmpty()){
                showToast(requireContext(),"비밀번호를 입력하세요")
            } else {
                login(loginData)
            }
        }

    }

//    private fun loadImage(imageUrl: String) : Bitmap? {
//        val bmp: Bitmap? = null
//        try {
//
//            val url = URL(imageUrl)
//            val stream = url.openStream()
//
//            return BitmapFactory.decodeStream(stream)
//
//        } catch (e: MalformedURLException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return bmp
//    }

    private fun imageLoadFromURL(imageUrl: String) {
        // Glide를 사용하여 이미지 로드
        Glide.with(requireContext())
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    // 이미지 로드가 완료
                    Log.d("이미지 로드", "성공")
                    // 예를 들어, 비트맵을 투명 배경으로 변경하는 경우:
                    val transparentBitmap = makeTransparentBitmap(resource)
                    if (transparentBitmap != null) {
                        Log.d("이미지 투명", "성공")
                    } else {
                        Log.d("이미지 투명", "실패")
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // 이미지 로드가 취소되거나 해제된 경우 호출
                }
            })
    }

    fun makeTransparentBitmap(sourceBitmap: Bitmap): Bitmap {
        val width = sourceBitmap.width
        val height = sourceBitmap.height

        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)

        // 배경을 투명하게 설정
        canvas.drawColor(Color.TRANSPARENT)

        // 원본 비트맵을 그림
        canvas.drawBitmap(sourceBitmap, 0f, 0f, null)

        return resultBitmap
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    private val retrofitService = RetrofitService().apiService
    private fun getLoginData(): LoginData {
        val username = binding.userIdInput.text.toString()
        val password = binding.userPwInput.text.toString()

        return LoginData(username, password)
    }
    private fun login(loginData: LoginData){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // API 요청 보내기
                val response = retrofitService.login(loginData)

                if (response.isSuccessful) {
                    // 로그인 성공
                    withContext(Dispatchers.Main) {
                        showToast(requireContext(), "로그인이 되었습니다.")

                        // 토큰 정보 저장 (SharedPreferences)
                        val token = response.body()?.token
                        val sharedPreferences = context?.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences?.edit()
                        editor?.putString("token", token)
                        editor?.apply()

                        val vo = realm.where(PlantModel::class.java).equalTo("P_Name", "rose49").findFirst()

                        if (vo != null) {
                            val P_Name = vo.P_Name
                            val P_Birth = vo.P_Birth
                            val P_Race = vo.P_Race
                            val P_Registration = vo.P_Registration

                            // main 화면으로 전환
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            intent.putExtra("P_Name", P_Name)
                            intent.putExtra("P_Birth", P_Birth)
                            intent.putExtra("P_Race", P_Race)
                            intent.putExtra("P_Registration", P_Registration)

                            requireActivity().startActivity(intent)
                        } else {
                            val P_Name = "식물 없음"
                            val P_Birth = "1900-01-01"
                            val P_Race = "종 없음"
                            val P_Registration = "미등록"

                            // main 화면으로 전환
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            intent.putExtra("P_Name", P_Name)
                            intent.putExtra("P_Birth", P_Birth)
                            intent.putExtra("P_Race", P_Race)
                            intent.putExtra("P_Registration", P_Registration)

                            requireActivity().startActivity(intent)
                        }

                    }
                } else {
                    // 로그인 실패
                    withContext(Dispatchers.Main) {
                        showToast(requireContext(), "로그인 실패")
                    }
                }
            } catch (e: Exception) {
                // API 요청 실패
                withContext(Dispatchers.Main) {
                    showToast(requireContext(), "API 요청 실패: ${e.message}")
                }
            }

        }
    }
        companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}