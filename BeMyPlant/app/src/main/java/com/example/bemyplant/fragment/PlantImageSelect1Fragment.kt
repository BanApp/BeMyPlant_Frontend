package com.example.bemyplant.fragment

import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentPlantImageSelect1Binding
import com.example.bemyplant.network.RetrofitService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlantImageSelect1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlantImageSelect1Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    val binding by lazy{ FragmentPlantImageSelect1Binding.inflate((layoutInflater))}
    private val retrofitService = RetrofitService().apiService2

    //private lateinit var binding: FragmentPlantRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding.nextButton.setOnClickListener{

//            val P_Name = binding.question1Edit.text.toString()
//            val P_Race = binding.question2Edit.text.toString()
//            val P_Color = binding.question3Edit.text.toString()
//            val Pot_Color = binding.question4Edit.text.toString()
//            Log.i("Search View ♥♥♥♥♥♥",P_Name)
//
//            val bundle = bundleOf(
//                "P_Name" to P_Name,
//                "P_Race" to P_Race,
//                "P_Color" to P_Color,
//                "Pot_Color" to Pot_Color
//            )

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
            }else {
//                var imageGenerateRequestData = GeneratePlantImageRequest(plantColor, plantSpecies, potColor)
                var imageGenerateRequestData = "asd"
                // API 요청 보냄
                // Show loading dialog
                val progressDialog = ProgressDialog(this.requireContext())
                progressDialog.setMessage("생성 중...")
                progressDialog.setCancelable(false)
                progressDialog.show()

//                lifecycleScope.launch {
//                    var imageURLs = plantImageGenerate(imageGenerateRequestData, progressDialog)
//                    if (imageURLs == null) {
//                        // bad request
//                        Log.d("식물 이미지생성", "식물 이미지 생성 결과 null")
//                    } else {
//                        Log.d("식물 이미지생성", "식물 이미지 생성완료 ")
//
//                        val imageURLs = imageURLs
//
//                        Log.d("식물 이미지생성결과", imageURLs.plant_image_urls.toString())
//
//                        if (imageURLs.plant_image_urls == null) {
//                            Log.d("식물 이미지생성 결과", "원소 없음 !!")
//                        }
//
//                        val bundle = bundleOf("plantName" to plantName, "plantSpecies" to plantSpecies, "plantColor" to plantColor, "potColor" to potColor, "imageURLs" to imageURLs.plant_image_urls)
//                        Log.d("bundle-f1", bundle.toString())
////                        findNavController().navigate(R.id.plantImageSelect2Fragment, bundle)
//                        findNavController().navigate(R.id.action_plantImageSelect1Fragment_to_plantImageSelect2Fragment, bundle)
//                    }
//                }
                val bundle = bundleOf("plantName" to plantName, "plantSpecies" to plantSpecies, "plantColor" to plantColor, "potColor" to potColor, "imageURLs" to "http://www.google.com")
                findNavController().navigate(R.id.action_plantImageSelect1Fragment_to_plantImageSelect2Fragment, bundle)
            }
        }

        return binding.root
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

//    private suspend fun plantImageGenerate(imageGenerateRequestData: GeneratePlantImageRequest, progressDialog: ProgressDialog): GeneratePlantImageResponse? {
//        val deferred = CompletableDeferred<GeneratePlantImageResponse?>()
//
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                // API 요청 보내기
//                val response = retrofitService.generatePlantImages(imageGenerateRequestData)
//                Log.d("식물 이미지 api 결과", response.toString())
//
//                if (response.isSuccessful) {
//                    // 이미지 받아오기 성공
//                    withContext(Dispatchers.Main) {
//                        Log.d("식물이미지생성", "식물 이미지 생성 완료")
//                        deferred.complete(response.body())
//                    }
//                } else {
//                    // 이미지 받아오기 실패
//                    withContext(Dispatchers.Main) {
//                        showToast(requireContext(), "식물 이미지 생성 실패")
//                        findNavController().navigate(R.id.plantImageSelect1Fragment)
//                        deferred.complete(null)
//                    }
//                }
//            } catch (e: Exception) {
//                // API 요청 실패
//                withContext(Dispatchers.Main) {
//                    showToast(requireContext(), "API 요청 실패: ${e.message}")
//                    deferred.complete(null)
//                }
//            } finally {
//                // Dismiss the loading dialog on the main thread
//                withContext(Dispatchers.Main) {
//                    progressDialog.dismiss()
//                }
//            }
//        }
//
//        return deferred.await()
//    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlantImageSelect1Fragment().apply {
                arguments = Bundle().apply { }
            }
    }
}