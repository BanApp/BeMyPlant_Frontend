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
import com.example.bemyplant.databinding.FragmentS2Binding
import com.example.bemyplant.network.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date


class s2Fragment : Fragment() {
    val binding by lazy{FragmentS2Binding.inflate((layoutInflater))}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
/*
* 입력받은*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.button3.setOnClickListener {
            val signUpData = getSignUpData()

            val pw2 = binding.editText3.text.toString()
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
        val username = binding.editText1.text.toString()
        val pw = binding.editText2.text.toString()
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