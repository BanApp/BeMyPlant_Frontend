package com.example.bemyplant.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.R
import com.example.bemyplant.data.SignUpData
import com.example.bemyplant.databinding.FragmentSignUp2Binding
import com.example.bemyplant.network.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import com.example.bemyplant.PlantRegisterForFragmentActivity
import com.example.bemyplant.data.LoginData
import io.realm.Realm


class SignUp2Fragment : Fragment() {
    lateinit var realm : Realm
    val binding by lazy{FragmentSignUp2Binding.inflate((layoutInflater))}
    lateinit var username : String
    lateinit var pw : String
    lateinit var r_name : String
    lateinit var phones : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    /*
    * 입력받은*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //비밀번호 마스킹 처리 코드
        binding.visibleButton.setOnClickListener {
            val currentTransformation = binding.userPwInput.transformationMethod
            if(currentTransformation is PasswordTransformationMethod) {
                binding.userPwInput.transformationMethod =
                    SingleLineTransformationMethod.getInstance()
            }else{
                binding.userPwInput.transformationMethod = PasswordTransformationMethod.getInstance()

            }
        }
        binding.visibleButton2.setOnClickListener {
            val currentTransformation = binding.userPwConfirmInput.transformationMethod
            if(currentTransformation is PasswordTransformationMethod){
                binding.userPwConfirmInput.transformationMethod = SingleLineTransformationMethod.getInstance()
            }else{
                binding.userPwConfirmInput.transformationMethod = PasswordTransformationMethod.getInstance()

            }

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.finishButton.setOnClickListener {
            val signUpData = getSignUpData()

            val pw2 = binding.userPwConfirmInput.text.toString()
            //findNavController().navigate(R.id.action_s2Fragment_to_plantImageSelect1Fragment)

            if (signUpData.username.isEmpty()){
                showToast(requireContext(),"아이디를 입력해주세요.")
            } else if (signUpData.password.isEmpty()){
                showToast(requireContext(),"비밀번호를 입력해주세요.")
            } else if (signUpData.password != pw2) {
                showToast(requireContext(),"비밀번호가 일치하지 않습니다.")
            } else{
                signUp(signUpData)
            }
        }
        return binding.root
    }
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    private val retrofitService = RetrofitService().apiService

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
                        findNavController().navigate(R.id.action_s2Fragment_to_userImageSelect1Fragment)
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

    private fun getSignUpData(): SignUpData {
        username = binding.userIdInput.text.toString()
        pw = binding.userPwInput.text.toString()
        r_name = arguments?.getString("r_name").toString()
        phones = arguments?.getString("phones").toString()
        var date = Date()

        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateStr: String = format.format(date)
        return SignUpData(username, pw, phones, r_name, dateStr, 1)
    }

    private fun signUp(signUpData: SignUpData){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // API 요청 보내기
                val response = retrofitService.signUp(signUpData)

                if (response.isSuccessful) {
                    // 회원 가입 성공
                    withContext(Dispatchers.Main) {
                        showToast(requireContext(), "회원가입이 완료되었습니다")
                        //TODO: 반드시 확인 , nav_graph 쪼갬
                        // plant register 화면으로 전환 (ACTIVITY 전환)
                        Log.d("회원가입" ,"회원가입 완료")

                        // 로그인 시도
//                        var loginData: LoginData = LoginData(username, pw)
//                        login(loginData)

                        findNavController().navigate(R.id.action_s2Fragment_to_plantImageSelect1Fragment)
                    }
                } else {
                    // 회원 가입 실패
                    withContext(Dispatchers.Main) {

                        showToast(requireContext(), "")
                        showToast(requireContext(), response.errorBody()!!.string())
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

}


/*
package com.example.bemyplant.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.R
import com.example.bemyplant.data.SignUpData
import com.example.bemyplant.databinding.FragmentSignUp2Binding
import com.example.bemyplant.network.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date


class SignUp2Fragment : Fragment() {
    val binding by lazy{FragmentSignUp2Binding.inflate((layoutInflater))}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

 입력받은
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.finishButton.setOnClickListener {
            val signUpData = getSignUpData()

            val pw2 = binding.userPwConfirmInput.text.toString()
            if (signUpData.username.isEmpty()){
                showToast(requireContext(),"아이디를 입력해주세요.")
            } else if (signUpData.password.isEmpty()){
                showToast(requireContext(),"비밀번호를 입력해주세요.")
            } else if (signUpData.password != pw2) {
                showToast(requireContext(),"비밀번호가 일치하지 않습니다.")
            } else{
                signUp(signUpData)
            }
        }
        return binding.root
    }
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    private val retrofitService = RetrofitService().apiService

    private fun getSignUpData(): SignUpData {
        val username = binding.userIdInput.text.toString()
        val pw = binding.userPwInput.text.toString()
        val username = binding.userIdInput.text.toString()
        val pw = binding.userPwConfirmInput.text.toString()
        val r_name = arguments?.getString("r_name").toString()
        val phones = arguments?.getString("phones").toString()
        val date = Date()

        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateStr: String = format.format(date)
        return SignUpData(username, pw, phones, r_name, dateStr, 1)
    }
    private fun signUp(signUpData: SignUpData){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // API 요청 보내기
                val response = retrofitService.signUp(signUpData)

                if (response.isSuccessful) {
                    // 회원 가입 성공
                    withContext(Dispatchers.Main) {
                        showToast(requireContext(), "회원가입이 완료되었습니다")
                        findNavController().navigate(R.id.action_s2Fragment_to_pRFragment)
                    }
                } else {
                    // 회원 가입 실패
                    withContext(Dispatchers.Main) {

                        showToast(requireContext(), "회원가입 실패")
                        showToast(requireContext(), response.errorBody()!!.string())
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

}
*/