package com.example.bemyplant.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import androidx.core.content.ContextCompat
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentMainBinding
import com.example.bemyplant.model.PlantModel
import com.example.bemyplant.module.PlantModule
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.RealmResults
import java.io.ByteArrayOutputStream
import java.net.URL

class MainFragment : Fragment() {
    val binding by lazy{FragmentMainBinding.inflate((layoutInflater))}
    lateinit var realm : Realm

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
        //글자 색 변경
        val changeAppNameText = SpannableString(resources.getString(R.string.main_name))
        val searchText = "PLANT"
        val startIndex = changeAppNameText.indexOf(searchText)
        if (startIndex != -1) {
            val endIndex = startIndex + searchText.length
            val colorText = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.green_1))
            changeAppNameText.setSpan(colorText, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }



        binding.appNameText.text = changeAppNameText

        binding.loginButton.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment2_to_loginFragment3)
        }
        binding.signButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment2_to_s1Fragment)
        }

//        var gURL : String = "https://blog.kakaocdn.net/dn/cAuwVb/btqE7mYami5/cq6e0C7VxP1xS4kRN2AAu1/img.png"
//
//        var vo = realm.where(PlantModel::class.java).findFirst()
//
//        if (vo != null) {
//            println("++++++++++++++++++")
////            println(vo.P_Name)
//            println(vo.P_Image)
//            var imgDate = vo.P_Image
//            var imgFinal = byteArrayToBitmap(imgDate)
////            Glide.with(requireContext())
////                .asBitmap()
////                .load(gURL)
////                .into(binding.testImg)
//            Glide.with(requireContext())
//                .asBitmap()
//                .load(gURL)
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(
//                        resource: Bitmap,
//                        transition: Transition<in Bitmap>?
//                    ) {
//                        // 이미지 로드가 완료
//                        Log.d("이미지 로드", "성공")
//                        // 예를 들어, 비트맵을 투명 배경으로 변경하는 경우:
//                        binding.testImg.setImageBitmap(resource)
//                        var finalByte = bitmapToByteArray(resource)
//                        realm.executeTransaction {
//                            //전부지우기
//                            it.where(PlantModel::class.java).findAll().deleteAllFromRealm()
//                            //첫번째 줄 지우기
//                            //            it.where(PlantModel::class.java).findFirst()?.deleteFromRealm()
//                        }
//                        realm.executeTransaction{
//                            with(it.createObject(PlantModel::class.java)){
//                                this.P_Name = "plantName"
//                                this.P_Birth = "bitrhDate"
//                                this.P_Race = "plantSpecies"
//                                if (finalByte != null) {
//                                    this.P_Image = finalByte
//                                }
//                                this.P_Registration = "regNum"
//                            }
//                        }
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                        TODO("Not yet implemented")
//                    }
//                })
//
//
////            binding.testImg.setImageBitmap(imgFinal)
//        } else {
//            println("-------------------")
//            println("null")
//        }

        return binding.root
    }

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

}