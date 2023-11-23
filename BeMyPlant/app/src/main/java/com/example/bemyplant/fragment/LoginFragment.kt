package com.example.bemyplant.fragment

import android.content.SharedPreferences
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bemyplant.MainActivity
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentLoginBinding
import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.PlantRegisterForFragmentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.bemyplant.data.LoginData
import com.example.bemyplant.data.LoginResponse
import com.example.bemyplant.data.SignUpData
import com.example.bemyplant.model.Diary
import com.example.bemyplant.model.PlantModel
import com.example.bemyplant.module.DiaryModule
import com.example.bemyplant.module.PlantModule
import com.example.bemyplant.network.RetrofitService
import io.realm.Realm
import io.realm.RealmConfiguration
import java.text.SimpleDateFormat
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

    lateinit var realm : Realm
//    lateinit var realm2 : Realm


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        Realm.init(requireContext())

        val configPlant : RealmConfiguration = RealmConfiguration.Builder()
            .name("appdb.realm") // 생성할 realm 파일 이름 지정
            .deleteRealmIfMigrationNeeded()
            .modules(PlantModule())
            .allowWritesOnUiThread(true) // sdhan : UI thread에서 realm에 접근할수 있게 허용
            .build()
        realm = Realm.getInstance(configPlant)

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

        realm.executeTransaction{
            with(it.createObject(PlantModel::class.java)){
                this.P_Name = "Rose"
                this.P_Birth = "2023-10-23"
                this.P_Race = "Rosaceae"
//                this.P_Image =
                this.P_Registration = "P_0001"
            }
        }

//        realm2.executeTransaction{
//            with(it.createObject(Diary::class.java)){
//                this.Title = "Rose2"
//                this.Content = "Rosaceae2"
//            }
//        }


//        // sdhan :realm DB control : DB 초기화 or 지우기
//        realm.executeTransaction {
//            //전부지우기
//            it.where(PlantModel::class.java).findAll().deleteAllFromRealm()
//            //첫번째 줄 지우기
////            it.where(PlantModel::class.java).findFirst()?.deleteFromRealm()
//        }

        // sdhan :realm DB control : 불러오기
        val vo = realm.where(PlantModel::class.java).equalTo("P_Name", "Rose").findFirst()
//        val vo2 = realm.where(Diary::class.java).equalTo("P_Name", "Rose2").findFirst()

        if (vo != null) {
            Log.d("++++++++++++++++++++++++++", vo.P_Name)
            Log.d("++++++++++++++++++++++++++", vo.P_Birth)
            Log.d("++++++++++++++++++++++++++", vo.P_Race)
            Log.d("++++++++++++++++++++++++++", vo.P_Registration)
        }

//        if (vo2 != null) {
//            Log.d("++++++++++++++++++++++++++", vo2.Title)
//        }

//        realm.executeTransaction {
//            //첫번째 줄을 가져와라
//            val data = it.where(PlantModel::class.java).findFirst()
//            Log.d("start", "start !!!!")
//            if (data != null) {
//                Log.d("PlantModel P_Name :", data.P_Name)
//            }
//            Log.d("end", "end !!!!")
//        }

        
        // sdhan : 날짜
//        val dateFormat = "yyyy-MM-dd HH:mm"
        val dateFormat = "yyyy-MM-dd"
        val dateFormat2 = "yyMMdd"
        val date = Date(System.currentTimeMillis())
        val simpleDateFormat = SimpleDateFormat(dateFormat)
        val simpleDateFormat2 = SimpleDateFormat(dateFormat2)

        val simpleDate: String = simpleDateFormat.format(date)
        val simpleDate2: String = simpleDateFormat2.format(date)
        Log.d("++++++++++++", "++++++++++++++++++++++++++")
        Log.d("++++++++++++", simpleDate)
        Log.d("++++++++++++", simpleDate2)

        // sdhan : 랜덤함수
        val range = (1000000..9999999)  // 1000000 <= n <= 9999999
        val randomNum = range.random()

        // 참고 - plantRegistration에서 P_Birth와 임의의 랜덤값을 이용해 식물 주민 등록번호를 생성할 것
        // sdhan : 등록번호 = 날짜 + 랜덤숫자
        val regNum = "${simpleDate2}-${randomNum}"
        Log.d("++++++++++++", "++++++++++++++++++++++++++")
        Log.d("++++++++++++", "++++++++++++++++++++++++++")
        Log.d("++++++++++++", "++++++++++++++++++++++++++")
        Log.d("++++++++++++", regNum)


        binding.startButton.setOnClickListener {
            val loginData = getLoginData()

            if (vo != null) {
                val P_Name = vo.P_Name
                val P_Birth = vo.P_Birth
                val P_Race = vo.P_Race
                val P_Registration = vo.P_Registration
                val bundle = bundleOf(
                    "P_Name" to P_Name,
                    "P_Birth" to P_Birth,
                    "P_Race" to P_Race,
                    "P_Registration" to P_Registration
                )
            }

            if (loginData.username.isEmpty()){
                showToast(requireContext(),"아이디를 입력하세요")
            }else if (loginData.password.isEmpty()){
                showToast(requireContext(),"비밀번호를 입력하세요")
            } else {
                login(loginData)
            }
//            realm.executeTransaction {
//                //첫번째 줄을 가져와라
//                val data = it.where(PlantModel::class.java).findFirst()
//                Log.d("start", "start !!!!")
//                if (data != null) {
//                    Log.d("PlantModel P_Name :", data.P_Name)
//                    Log.d("PlantModel P_Name :", data.P_Birth)
//                    Log.d("PlantModel P_Name :", data.P_Race)
//                    Log.d("PlantModel P_Name :", data.P_Registration)
//                }
//                Log.d("end", "end !!!!")
//            }
        }
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

                        val vo = realm.where(PlantModel::class.java).equalTo("P_Name", "Rose").findFirst()

                        val P_Name = vo?.P_Name
                        val P_Birth = vo?.P_Birth
                        val P_Race = vo?.P_Race
                        val P_Registration = vo?.P_Registration

//                        Log.d("+++++++", "+++++++")
//                        if (P_Birth != null) {
//                            Log.d("P_Birth", P_Birth)
//                        }

                        // main 화면으로 전환
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        intent.putExtra("P_Name", P_Name)
                        intent.putExtra("P_Birth", P_Birth)
                        intent.putExtra("P_Race", P_Race)
                        intent.putExtra("P_Registration", P_Registration)

                        requireActivity().startActivity(intent)
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