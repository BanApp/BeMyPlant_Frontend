package com.example.bemyplant.fragment

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.R
import com.example.bemyplant.data.GenerateUserImageRequest
import com.example.bemyplant.data.GenerateUserImageResponse
import com.example.bemyplant.databinding.FragmentUserImageSelect1Binding
import com.example.bemyplant.network.RetrofitService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserImageSelect1Fragment : Fragment() {
    val binding by lazy{FragmentUserImageSelect1Binding.inflate(layoutInflater)}
    private val retrofitService = RetrofitService().apiService2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

//        binding.skipButton.setOnClickListener {
//            val intent = Intent(requireActivity(), TempConnectActivity::class.java)
//            requireActivity().startActivity(intent)
//        }

        binding.nextButton.setOnClickListener {
            var gender:String = ""
            var characteristic = binding.featureEdit.text.toString()

            val sexGroup: RadioGroup = binding.sexGroup
            val selectedRadioButtonId: Int = sexGroup.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton: RadioButton = binding.root.findViewById<RadioButton>(selectedRadioButtonId)
                val selectedText: String = selectedRadioButton.text.toString()
                gender = selectedText
            } else {
                showToast(requireContext(),"성별을 선택해주세요")
            }

            // 특징은 적지 않아도 이미지 생성 가능 (default string 지정)
            if (characteristic == null){
                characteristic = "웃고 있는, 단정한 머리, 검정 머리와 검정 눈"
            }

            var imageGenerateRequestData = GenerateUserImageRequest(gender, characteristic)
            // API 요청 보냄
            // Show loading dialog
            val progressDialog = ProgressDialog(this.requireContext())
            progressDialog.setMessage("생성 중...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            lifecycleScope.launch {
                var imageURLs = userImageGenerate(imageGenerateRequestData, progressDialog)
                if (imageURLs == null) {
                    // bad request
                    Log.d("식물 이미지생성", "식물 이미지 생성 결과 null")
                } else {
                    Log.d("식물 이미지생성", "식물 이미지 생성완료 ")

                    val imageURLs = imageURLs

                    Log.d("식물 이미지생성결과", imageURLs.user_image_urls.toString())

                    if (imageURLs.user_image_urls == null) {
                        Log.d("식물 이미지생성 결과", "원소 없음 !!")
                    }

                    val bundle = bundleOf("gender" to gender, "characteristic" to characteristic, "imageURLs" to imageURLs.user_image_urls)
                    Log.d("bundle-f1", bundle.toString())
                    findNavController().navigate(R.id.action_userImageSelect1Fragment_to_userImageSelect2Fragment,bundle)

                }
            }




        }
        return binding.root
    }


    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private suspend fun userImageGenerate(imageGenerateRequestData: GenerateUserImageRequest, progressDialog: ProgressDialog): GenerateUserImageResponse? {
        val deferred = CompletableDeferred<GenerateUserImageResponse?>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // API 요청 보내기
                val response = retrofitService.generateUserImages(imageGenerateRequestData)
                Log.d("사용자 이미지 api 결과", response.toString())

                if (response.isSuccessful) {
                    // 이미지 받아오기 성공
                    withContext(Dispatchers.Main) {
                        Log.d("사용자 이미지생성", "사용자 이미지 생성 완료")
                        deferred.complete(response.body())
                    }
                } else {
                    // 이미지 받아오기 실패
                    withContext(Dispatchers.Main) {
                        showToast(requireContext(), "사용자 이미지 생성 실패")
                        //findNavController().navigate()
                        deferred.complete(null)
                    }
                }
            } catch (e: Exception) {
                // API 요청 실패
                withContext(Dispatchers.Main) {
                    showToast(requireContext(), "API 요청 실패: ${e.message}")
                    deferred.complete(null)
                }
            } finally {
                // Dismiss the loading dialog on the main thread
                withContext(Dispatchers.Main) {
                    progressDialog.dismiss()
                }
            }
        }
        return deferred.await()
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            UserImageSelect1Fragment().apply {
                arguments = Bundle().apply { }
            }
    }
}