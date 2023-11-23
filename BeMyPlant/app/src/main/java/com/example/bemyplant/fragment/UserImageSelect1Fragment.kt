package com.example.bemyplant.fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.R
import com.example.bemyplant.TempConnectActivity
import com.example.bemyplant.data.GenerateUserImageRequest
import com.example.bemyplant.data.GenerateUserImageResponse
import com.example.bemyplant.databinding.FragmentUserImageSelect1Binding
import com.example.bemyplant.network.RetrofitService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserImageSelect1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserImageSelect1Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    val binding by lazy{FragmentUserImageSelect1Binding.inflate(layoutInflater)}
    private val retrofitService = RetrofitService().apiService2
    private lateinit var plantName: String
    private lateinit var plantSpecies: String
    private lateinit var plantColor: String
    private lateinit var potColor: String
//    private lateinit var imageURLs: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getImageGenerateData()
        // Inflate the layout for this fragment

        binding.skipButton.setOnClickListener {
            val intent = Intent(requireActivity(), TempConnectActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding.nextButton.setOnClickListener {
            var gender: String = ""
            val characteristic = binding.featureEdit.text.toString()

            val sexGroup: RadioGroup = binding.sexGroup
            val selectedRadioButtonId: Int = sexGroup.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton: RadioButton =
                    binding.root.findViewById<RadioButton>(selectedRadioButtonId)
                val selectedText: String = selectedRadioButton.text.toString()
                gender = selectedText
            } else {
                showToast(requireContext(), "성별을 선택해주세요")
            }
            // 특징은 적지 않아도 이미지 생성 가능
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

//                    Log.d("식물 이미지생성결과", imageURLs.user_image_urls.toString())

//                    if (imageURLs.user_image_urls == null) {
//                        Log.d("식물 이미지생성 결과", "원소 없음 !!")
//                    }
//            val bundle = bundleOf("plantName" to plantName, "plantSpecies" to plantSpecies, "plantColor" to plantColor, "potColor" to potColor, "imageURLs" to "http://www.google.com")
                    val bundle = bundleOf(
                        "gender" to gender,
                        "characteristic" to characteristic,
//                        "imageURLs" to imageURLs.user_image_urls
                    )
                    findNavController().navigate(
                        R.id.action_userImageSelect1Fragment_to_userImageSelect2Fragment,
                        bundle
                    )
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
                        findNavController().navigate(R.id.plantImageSelect1Fragment)
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

    private fun getImageGenerateData() {
        //Log.d("bundle-f2", arguments?.getStringArrayList("imageURLs").toString())
        plantName = arguments?.getString("plantName").toString()
        plantSpecies = arguments?.getString("plantSpecies").toString()
        plantColor = arguments?.getString("plantColor").toString()
        potColor = arguments?.getString("potColor").toString()
//        imageURLs = arguments?.getStringArrayList("imageURLs") ?: emptyList<String>()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment userImageSelectFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserImageSelect1Fragment().apply {
                arguments = Bundle().apply { }
            }
    }
}