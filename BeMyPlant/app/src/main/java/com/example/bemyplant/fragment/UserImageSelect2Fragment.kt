package com.example.bemyplant.fragment

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.bemyplant.R
import com.example.bemyplant.TempConnectActivity
import com.example.bemyplant.databinding.FragmentUserImageSelect2Binding
import com.example.bemyplant.model.PlantModel
import com.example.bemyplant.module.PlantModule
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.concurrent.thread

private lateinit var imageURLs: List<String>
private lateinit var gender: String
private lateinit var characteristic: String

private var selectedImage: Bitmap? = null

class UserImageSelect2Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    val binding by lazy{FragmentUserImageSelect2Binding.inflate(layoutInflater)}

    private lateinit var plantName: String
    private lateinit var plantSpecies: String
    private lateinit var plantColor: String
    private lateinit var potColor: String
    private lateinit var plantImageURLs: List<String>
    private lateinit var userImageURLs: List<String>

    private lateinit var realm : Realm

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

        val configPlant : RealmConfiguration = RealmConfiguration.Builder()
            .name("appdb.realm") // 생성할 realm 파일 이름 지정
            .deleteRealmIfMigrationNeeded()
            .modules(PlantModule())
            .allowWritesOnUiThread(true) // sdhan : UI thread에서 realm에 접근할수 있게 허용
            .build()
        realm = Realm.getInstance(configPlant)

        // 이전화면에서 넘어온 데이터 저장 (성별, 특징, url)
        gender = arguments?.getString("gender").toString()
        characteristic = arguments?.getString("characteristic").toString()
        plantImageURLs = arguments?.getStringArrayList("plantImageURLs") ?: emptyList<String>()
        userImageURLs = arguments?.getStringArrayList("userImageURLs") ?: emptyList<String>()

        binding.tagText.text = "#${gender}#${characteristic}"
        
        var shownImageCount = 0
        var userImageURLsCount = userImageURLs.size
        Log.d("사용자 이미지 개수", userImageURLs.size.toString())

        // 이미지 2개만 고치기
//        setTwoImages(
//            imageURLs[shownImageCount],
//            imageURLs[shownImageCount + 1]
//        )
//        shownImageCount += 2
        // 테스트코드
        if (userImageURLs.isNotEmpty() && userImageURLsCount >= 2) {
            setTwoImages(
                userImageURLs[shownImageCount],
                userImageURLs[shownImageCount + 1]
            )
            shownImageCount += 2
        }

        binding.refreshButton.setOnClickListener {
            Log.d("식물 이미지 업데이트 횟수", shownImageCount.toString())
            if ((shownImageCount + 2) <= userImageURLsCount) { // 둘 다 변경 가능하면 변경
                setTwoImages(
                    userImageURLs[shownImageCount],
                    userImageURLs[shownImageCount + 1]
                )
                Log.d("식물 이미지 업데이트", "업데이트 완료")
                shownImageCount += 2
            } else if ((shownImageCount + 1 ) <= userImageURLsCount) { // 하나라도 변경 가능하면 변경
                setOneImages(userImageURLs[shownImageCount]) //button1 수정
                //binding.plantImageButton2.setImageBitmap((imageURLs[shownImageCount]))
                Log.d("식물 이미지 업데이트", "업데이트 완료")
                shownImageCount += 1
            } else {
                // 큐에 쌓여 있는 나머지 이미지 생성, 만일 더이상 보여줄 이미지가 없다면 -> 클릭 불가, 투명도 증가로 수정
                binding.refreshButton.isEnabled = false // 클릭 불가
                binding.refreshButton.alpha = 0.5f // 투명도 증가
            }
        }
        binding.userImageButton1.setOnClickListener {
            // 투명도 조절 (클릭 효과)
            binding.userImageButton1.alpha = 1f
            binding.userImageButton2.alpha = 0.5f

            binding.userImageButton1.setBackgroundResource(R.drawable.image_select)
            binding.userImageButton1.setBackgroundResource(0)
            selectedImage = drawableToBitmap(binding.userImageButton1.drawable)
        }

        binding.userImageButton2.setOnClickListener {
            // 투명도 조절 (클릭 효과)
            binding.userImageButton1.alpha = 0.5f
            binding.userImageButton2.alpha = 1f

            binding.userImageButton2.setBackgroundResource(R.drawable.image_select)
            binding.userImageButton2.setBackgroundResource(0)
            selectedImage = drawableToBitmap(binding.userImageButton2.drawable)
        }

        binding.nextButton.setOnClickListener {
            Log.d("selectedImage", selectedImage.toString())
            if (selectedImage == null) {
                Log.d("selectedImage error", "selected image err")
                showToast(requireContext(), "유저 이미지를 선택해주세요")
            }
            else{
                Log.d("selectedImage-else", selectedImage.toString())
                saveBitmapToFile(requireContext(), selectedImage!!)

                val intent = Intent(requireActivity(), TempConnectActivity::class.java)
                requireActivity().startActivity(intent)

            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextButton.setOnClickListener{

            getImageGenerateData()

            // sdhan : 현재 날짜를 구해 P_Birth 연산하고 DB에 넣을 것
            val dateFormat = "yyyy-MM-dd"
            val now = Date(System.currentTimeMillis())
            val simpleDateFormat = SimpleDateFormat(dateFormat)
            val bitrhDate: String = simpleDateFormat.format(now)

            // sdhan : 등록번호용 날짜형식 생성
            val dateFormat2 = "yyMMdd"
            val simpleDateFormat2 = SimpleDateFormat(dateFormat2)
            val regDate: String = simpleDateFormat2.format(now)

            // sdhan : 랜덤함수
            val range = (1000000..9999999)  // 100000 <= n <= 999999
            println(range.random())

            // 참고 - plantRegistration에서 P_Birth와 임의의 랜덤값을 이용해 식물 주민 등록번호를 생성할 것
            // sdhan : 등록번호 = 날짜 + 랜덤숫자
            val regNum = "${regDate}-${range.random()}"

            Log.d("plantName", plantName)
            Log.d("bitrhDate", bitrhDate)
            Log.d("plantSpecies", plantSpecies)
            Log.d("plantImageURLs", plantImageURLs.toString())
            Log.d("regNum", regNum)

            realm.executeTransaction{
                with(it.createObject(PlantModel::class.java)){
                    this.P_Name = plantName
                    this.P_Birth = bitrhDate
                    this.P_Race = plantSpecies
//                    this.P_Image = plantImageURLs as ByteArray
                    this.P_Registration = regNum
                }
            }

            val vo = realm.where(PlantModel::class.java).equalTo("P_Name", plantName).findFirst()

            if (vo != null) {
                Log.d("realm : "+"vo.P_Name", vo.P_Name)
                Log.d("realm : "+"vo.P_Birth", vo.P_Birth)
                Log.d("realm : "+"vo.P_Race", vo.P_Race)
//                Log.d("realm : "+"vo.P_Image", vo.P_Image.toString())
                Log.d("realm : "+"vo.P_Registration", vo.P_Registration)
            }


//            realm.executeTransaction{
//                with(it.createObject(PlantModel::class.java)){
//                    val date = Date()
//                    val format = SimpleDateFormat("yyyy-MM-dd")
//                    val dateStr: String = format.format(date)
//                    val range = (100000..999999)  // 100000 <= n <= 999999
//                    val regist_num = range.random().toString()
//
//                    this.P_Name = plantName
//                    this.P_Birth = dateStr
//                    this.P_Race = plantSpecies
////                    this.P_Image = imageURLs
//                    this.P_Registration = regist_num
//                }
//            }
            val bundle = bundleOf("plantName" to plantName, "plantSpecies" to plantSpecies, "plantColor" to plantColor, "potColor" to potColor, "plantImageURLs" to plantImageURLs, "userImageURLs" to userImageURLs, "gender" to gender, "characteristic" to characteristic)
            Log.d("bundle-f4", bundle.getString("plantName").toString())
            Log.d("bundle-f4", bundle.getString("plantSpecies").toString())
            Log.d("bundle-f4", bundle.getString("plantColor").toString())
            Log.d("bundle-f4", bundle.getString("potColor").toString())
            Log.d("bundle-f4", bundle.getStringArrayList("plantImageURLs").toString())
            Log.d("bundle-f4", bundle.getStringArrayList("userImageURLs").toString())
            val intent = Intent(requireActivity(), TempConnectActivity::class.java)
            requireActivity().startActivity(intent)
        }
    }

    private fun getImageGenerateData() {
        //Log.d("bundle-f2", arguments?.getStringArrayList("imageURLs").toString())
        plantName = arguments?.getString("plantName").toString()
        plantSpecies = arguments?.getString("plantSpecies").toString()
        plantColor = arguments?.getString("plantColor").toString()
        potColor = arguments?.getString("potColor").toString()
        plantImageURLs = arguments?.getStringArrayList("plantImageURLs") ?: emptyList<String>()
        userImageURLs = arguments?.getStringArrayList("userImageURLs") ?: emptyList<String>()
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

    // 비트맵을 파일로 저장하는 함수
    fun saveBitmapToFile(context: Context, bitmap: Bitmap) {
        Log.d("saveBitmapToFile", "saveBitmapToFile 진입")
        Log.d("saveBitmapToFile-bitmap", bitmap.toString())

        var outputStream: OutputStream? = null

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "BEMYPLANT_USER_IMAGE.jpg")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                outputStream = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(storageDir, "BEMYPLANT_USER_IMAGE.jpg")
            outputStream = FileOutputStream(image)
        }

        outputStream?.let {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            it.flush()
            it.close()
            Log.d("saveBitmapToFile", "saveBitmapToFile end")
        }
    }

    interface ImageLoadCallback {
        fun onImageLoaded(bitmap: Bitmap)
        fun onImageLoadFailed()
    }
    private fun imageLoadFromURL0(imageUrl: String, callback: ImageLoadCallback) {
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
    private fun imageLoadFromURL(url: String, callback: ImageLoadCallback) {
        try {
            val bitmap = BitmapFactory.decodeStream(URL(url).openStream())
            callback.onImageLoaded(bitmap)
        } catch (e: Exception) {
            callback.onImageLoadFailed()
            e.printStackTrace()
        }
    }
    private fun setTwoImages(url1: String, url2: String) {
        // 생성중 이미지 선택
        // TODO: 생성중 ... 메시지 추가
        binding.textOverlay1.visibility = View.VISIBLE
        binding.textOverlay2.visibility = View.VISIBLE
        binding.userImageButton1.visibility = View.INVISIBLE
        binding.userImageButton2.visibility = View.INVISIBLE

        // Create a Handler associated with the main Looper
        val mainHandler = Handler(Looper.getMainLooper())

        // Call imageLoadFromURL for url1 in a separate thread
        thread {
            imageLoadFromURL(url1, object : ImageLoadCallback {
                override fun onImageLoaded(bitmap: Bitmap) {
                    // Use the loaded bitmap here
                    mainHandler.post {
                        binding.textOverlay1.visibility = View.INVISIBLE
                        binding.userImageButton1.visibility = View.VISIBLE
                        binding.userImageButton1.setImageBitmap(bitmap)
                    }
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
                        binding.textOverlay2.visibility = View.INVISIBLE
                        binding.userImageButton2.visibility = View.VISIBLE
                        binding.userImageButton2.setImageBitmap(bitmap)
                    }
                }

                override fun onImageLoadFailed() {
                    Log.d("이미지 url->비트맵", "2 실패")
                }
            })
        }


        // TODO: 만일 받아온 이미지가 null이라면 .. 처리
    }
    private fun setOneImages(url1: String) {
        binding.textOverlay1.visibility = View.VISIBLE
        binding.userImageButton1.visibility = View.INVISIBLE

        imageLoadFromURL(url1, object : ImageLoadCallback {
            override fun onImageLoaded(bitmap: Bitmap) {
                // Use the loaded bitmap here
                Log.d("이미지 url->비트맵", "1 성공")
                binding.textOverlay1.visibility = View.INVISIBLE
                binding.userImageButton1.visibility = View.VISIBLE
                binding.userImageButton1.setImageBitmap(bitmap)
            }

            override fun onImageLoadFailed() {
                Log.d("이미지 url->비트맵", "1 실패")
            }
        })

//        binding.plantImageButton1.setImageBitmap(imageLoadFromURL(url1))
        // TODO: 만일 받아온 이미지가 null이라면 .. 처리
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    fun drawableToBitmap(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UserImageSelect2Fragment().apply {
                arguments = Bundle().apply {}
            }
    }
}