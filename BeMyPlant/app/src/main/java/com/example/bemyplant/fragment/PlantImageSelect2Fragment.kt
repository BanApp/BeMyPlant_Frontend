package com.example.bemyplant.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.bemyplant.R
import com.example.bemyplant.TempConnectActivity
import com.example.bemyplant.databinding.FragmentPlantImageSelect2Binding
import com.example.bemyplant.model.PlantModel
import com.example.bemyplant.module.PlantModule
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread

class PlantImageSelect2Fragment : Fragment() {
    val binding by lazy{ FragmentPlantImageSelect2Binding.inflate((layoutInflater))}
    // TODO: Rename and change types of parameters
    private lateinit var plantName: String
    private lateinit var plantSpecies: String
    private lateinit var plantColor: String
    private lateinit var potColor: String
    private lateinit var plantImageURLs: List<String>
    private var selectedImage: Bitmap? = null
    private lateinit var realm: Realm
    private lateinit var plantImgSelected : ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val configPlant : RealmConfiguration = RealmConfiguration.Builder()
            .name("appdb.realm") // 생성할 realm 파일 이름 지정
            .deleteRealmIfMigrationNeeded()
            .modules(PlantModule())
            .allowWritesOnUiThread(true) // sdhan : UI thread에서 realm에 접근할수 있게 허용
            .build()
        realm = Realm.getInstance(configPlant)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getImageGenerateData() // 이전 화면으로부터 넘어오는 데이터저장 (plantName, plantSpecies, plantColor, potCOlor)
        Log.d("식물 이미지 가져옴", plantImageURLs.toString())

        binding.tagText.text = "#${plantSpecies}#${plantColor}#${potColor} 화분"

        var shownImageCount = 0
        var plantImageURLsCount = plantImageURLs.size

        Log.d("식물 이미지 개수", plantImageURLs.size.toString())

        // 이미지 2개만 고치기
        setTwoImages(
            plantImageURLs[shownImageCount],
            plantImageURLs[shownImageCount + 1]
        )
        shownImageCount += 2
        Log.d("식물 이미지 업데이트", "업데이트 완료")

        binding.refreshButton.setOnClickListener {
            Log.d("식물 이미지 업데이트 횟수", shownImageCount.toString())
            if ((shownImageCount + 2) <= plantImageURLsCount) { // 둘 다 변경 가능하면 변경
                setTwoImages(
                    plantImageURLs[shownImageCount],
                    plantImageURLs[shownImageCount + 1]
                )
                Log.d("식물 이미지 업데이트", "업데이트 완료")
                shownImageCount += 2
            } else if ((shownImageCount + 1 ) <= plantImageURLsCount) { // 하나라도 변경 가능하면 변경
                setOneImages(plantImageURLs[shownImageCount]) //button1 수정
                //binding.plantImageButton2.setImageBitmap((imageURLs[shownImageCount]))
                Log.d("식물 이미지 업데이트", "업데이트 완료")
                shownImageCount += 1
            } else {
                // 큐에 쌓여 있는 나머지 이미지 생성, 만일 더이상 보여줄 이미지가 없다면 -> 클릭 불가, 투명도 증가로 수정
                binding.refreshButton.isEnabled = false // 클릭 불가
                binding.refreshButton.alpha = 0.5f // 투명도 증가
            }
        }
        binding.plantImageButton1.setOnClickListener {
            // 투명도 조절 (클릭 효과)
            binding.plantImageButton1.alpha = 1f
            binding.plantImageButton2.alpha = 0.5f

            binding.plantImageButton1.setBackgroundResource(R.drawable.image_select)
            binding.plantImageButton1.setBackgroundResource(0)
            selectedImage = binding.plantImageButton1.drawable.toBitmap()
        }

        binding.plantImageButton2.setOnClickListener {
            // 투명도 조절 (클릭 효과)
            binding.plantImageButton1.alpha = 0.5f
            binding.plantImageButton2.alpha = 1f

            binding.plantImageButton2.setBackgroundResource(R.drawable.image_select)
            binding.plantImageButton2.setBackgroundResource(0)
            selectedImage = binding.plantImageButton2.drawable.toBitmap()
        }

        binding.nextButton.setOnClickListener {

            if (selectedImage == null) {
                showToast(requireContext(), "식물 이미지를 선택해주세요")
            }
            else{
                // TODO: (정현) 앱 내 식물 DB에 넣을 것 (식물 이름 종, 이미지, 생성 시간 ,식물 등록번호)
                //   참고 - 현재 날짜를 구해 P_Birth 연산하고 DB에 넣을 것
                //   참고 - plantRegistration에서 P_Birth와 임의의 랜덤값을 이용해 식물 주민 등록번호를 생성할 것
                plantImgSelected = bitmapToByteArray(selectedImage!!)
                val bundle = bundleOf(
                    "plantName" to plantName,
                    "plantSpecies" to plantSpecies,
                    "plantColor" to plantColor,
                    "potColor" to potColor,
                    "plantImageURLs" to plantImageURLs,
                    "plantImgSelected" to plantImgSelected
                )
                Log.d("bundle-f2", bundle.getString("plantName").toString())
                Log.d("bundle-f2", bundle.getString("plantSpecies").toString())
                Log.d("bundle-f2", bundle.getString("plantColor").toString())
                Log.d("bundle-f2", bundle.getString("potColor").toString())
                Log.d("bundle-f2", bundle.getStringArrayList("plantImageURLs").toString())
                Log.d("bundle-f2", bundle.getByteArray("plantImgSelected").toString())
//                findNavController().navigate(
//                    R.id.action_plantImageSelect2Fragment_to_userImageSelect1Fragment,
//                    bundle
//                )
                // TODO: bundle 값 넘기게 수정
                val intent = Intent(requireActivity(), TempConnectActivity::class.java)
                requireActivity().startActivity(intent)
            }
        }

        return binding.root
    }

    private fun getImageGenerateData() {
        //Log.d("bundle-f2", arguments?.getStringArrayList("imageURLs").toString())
        plantName = arguments?.getString("plantName").toString()
        plantSpecies = arguments?.getString("plantSpecies").toString()
        plantColor = arguments?.getString("plantColor").toString()
        potColor = arguments?.getString("potColor").toString()
        plantImageURLs = arguments?.getStringArrayList("plantImageURLs") ?: emptyList<String>()
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

    interface ImageLoadCallback {
        fun onImageLoaded(bitmap: Bitmap)
        fun onImageLoadFailed()
    }
    private fun imageLoadFromURL(imageUrl: String, callback: ImageLoadCallback) {
        // Glide를 사용하여 이미지 로드
        Glide.with(requireContext())
            .asBitmap()
            .load(imageUrl)
            .override(200,200)
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
                        callback.onImageLoaded(transparentBitmap)
                    } else {
                        callback.onImageLoadFailed()
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // 이미지 로드가 취소되거나 해제된 경우 호출
                }
            })
    }


    private fun setTwoImages(url1: String, url2: String) {
        // 생성중 이미지 선택
        // TODO: 생성중 ... 메시지 추가
        binding.textOverlay1.visibility = View.VISIBLE
        binding.textOverlay2.visibility = View.VISIBLE
        binding.plantImageButton1.visibility = View.INVISIBLE
        binding.plantImageButton2.visibility = View.INVISIBLE

        // Create a Handler associated with the main Looper
        val mainHandler = Handler(Looper.getMainLooper())

        // Call imageLoadFromURL for url1 in a separate thread
        thread {
            imageLoadFromURL(url1, object : ImageLoadCallback {
                override fun onImageLoaded(bitmap: Bitmap) {
                    // Use the loaded bitmap here
                    mainHandler.post {
                        Log.d("이미지 url->비트맵", "1 성공")
                        binding.textOverlay1.visibility = View.INVISIBLE
                        binding.plantImageButton1.visibility = View.VISIBLE
                        binding.plantImageButton1.setImageBitmap(bitmap)
                    }
                    var transBitmapToByteArray = bitmapToByteArray(bitmap)
                }

                override fun onImageLoadFailed() {
                    Log.d("이미지 url->비트맵", "1 실패")
                }
            })
        }

        // Call imageLoadFromURL for url2 in a separate thread
        thread {
            imageLoadFromURL(url2, object : ImageLoadCallback {
                override fun onImageLoaded(bitmap: Bitmap) {
                    // Use the loaded bitmap here
                    mainHandler.post {
                        Log.d("이미지 url->비트맵", "2 성공")
                        binding.textOverlay2.visibility = View.INVISIBLE
                        binding.plantImageButton2.visibility = View.VISIBLE
                        binding.plantImageButton2.setImageBitmap(bitmap)
                    }
                }

                override fun onImageLoadFailed() {
                    Log.d("이미지 url->비트맵", "2 실패")
                }
            })
        }

        // TODO: 만일 받아온 이미지가 null이라면 .. 처리
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    private fun setOneImages(url1: String) {
        binding.textOverlay1.visibility = View.VISIBLE
        binding.plantImageButton1.visibility = View.INVISIBLE

        imageLoadFromURL(url1, object : ImageLoadCallback {
            override fun onImageLoaded(bitmap: Bitmap) {
                // Use the loaded bitmap here
                Log.d("이미지 url->비트맵", "1 성공")
                binding.textOverlay1.visibility = View.INVISIBLE
                binding.plantImageButton1.visibility = View.VISIBLE
                binding.plantImageButton1.setImageBitmap(bitmap)
            }

            override fun onImageLoadFailed() {
                Log.d("이미지 url->비트맵", "1 실패")
            }
        })

//        binding.plantImageButton1.setImageBitmap(imageLoadFromURL(url1))
        // TODO: 만일 받아온 이미지가 null이라면 .. 처리
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlantImageSelect2Fragment().apply {
                arguments = Bundle().apply {}
            }
    }
}