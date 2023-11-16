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
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.bemyplant.data.LoginData
import com.example.bemyplant.data.LoginResponse
import com.example.bemyplant.data.SignUpData
import com.example.bemyplant.network.RetrofitService
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

                        // main 화면으로 전환
                        val intent = Intent(requireActivity(), MainActivity::class.java)
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