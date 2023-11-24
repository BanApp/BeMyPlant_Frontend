package com.example.bemyplant.fragment

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.R
import com.example.bemyplant.data.GeneratePlantImageRequest
import com.example.bemyplant.data.GeneratePlantImageResponse
import com.example.bemyplant.databinding.FragmentPlantImageSelect1Binding
import com.example.bemyplant.network.RetrofitService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlantImageSelect1Fragment : Fragment() {
    val binding by lazy{ FragmentPlantImageSelect1Binding.inflate((layoutInflater))}
    private val retrofitService = RetrofitService().apiService2

    //private lateinit var binding: FragmentPlantRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.nextButton.setOnClickListener{
            val plantName = binding.question1Edit.text.toString()
            val plantSpecies = binding.question2Edit.text.toString()
            val plantColor = binding.question3Edit.text.toString()
            val potColor = binding.question4Edit.text.toString()


            if (plantName == "" ){
                showToast(requireContext(),"식물 이름을 입력해주세요")
            } else if (plantSpecies== ""){
                showToast(requireContext(),"식물 품종을 입력해주세요")
            } else if (plantColor== ""){
                showToast(requireContext(),"식물 색상을 입력해주세요")
            }
            else if (potColor== ""){
                showToast(requireContext(),"화분 색상을 입력해주세요")
            }
            else {
                var imageGenerateRequestData = GeneratePlantImageRequest(plantColor, plantSpecies, potColor)
                // API 요청 보냄
                // Show loading dialog
                val progressDialog = ProgressDialog(this.requireContext())
                progressDialog.setMessage("생성 중...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                lifecycleScope.launch {
                    var plantImageURLs = plantImageGenerate(imageGenerateRequestData, progressDialog)
                    if (plantImageURLs == null) {
                        // bad request
                        Log.d("식물 이미지생성", "식물 이미지 생성 결과 null")
                    } else {
                        Log.d("식물 이미지생성", "식물 이미지 생성완료 ")

                        val plantImageURLs = plantImageURLs

                        Log.d("식물 이미지생성결과", plantImageURLs.plant_image_urls.toString())

                        if (plantImageURLs.plant_image_urls == null) {
                            Log.d("식물 이미지생성 결과", "원소 없음 !!")
                        }

                        val bundle = bundleOf("plantName" to plantName, "plantSpecies" to plantSpecies, "plantColor" to plantColor, "potColor" to potColor, "plantImageURLs" to plantImageURLs.plant_image_urls)
                        Log.d("bundle-f1", bundle.getString("plantName").toString())
                        Log.d("bundle-f1", bundle.getString("plantSpecies").toString())
                        Log.d("bundle-f1", bundle.getString("plantColor").toString())
                        Log.d("bundle-f1", bundle.getString("potColor").toString())
                        Log.d("bundle-f1", bundle.getStringArrayList("plantImageURLs").toString())
                        findNavController().navigate(R.id.action_plantImageSelect1Fragment_to_plantImageSelect2Fragment, bundle)

                    }
                }
            }
            //findNavController().navigate(R.id.action_plantImageSelect1Fragment_to_plantImageSelect2Fragment)
        }
        return binding.root
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private suspend fun plantImageGenerate(imageGenerateRequestData: GeneratePlantImageRequest, progressDialog: ProgressDialog): GeneratePlantImageResponse? {
        val deferred = CompletableDeferred<GeneratePlantImageResponse?>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // API 요청 보내기
                val response = retrofitService.generatePlantImages(imageGenerateRequestData)
                Log.d("식물 이미지 api 결과", response.toString())

                if (response.isSuccessful) {
                    // 이미지 받아오기 성공
                    withContext(Dispatchers.Main) {
                        Log.d("식물이미지생성", "식물 이미지 생성 완료")
                        deferred.complete(response.body())
                    }
                } else {
                    // 이미지 받아오기 실패
                    withContext(Dispatchers.Main) {
                        showToast(requireContext(), "식물 이미지 생성 실패")
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


    companion object {
        @JvmStatic
        fun newInstance() =
            PlantImageSelect1Fragment().apply {
                arguments = Bundle().apply { }
            }
    }
}