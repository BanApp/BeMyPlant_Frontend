package com.example.bemyplant.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.example.bemyplant.R
import com.example.bemyplant.model.PlantModel
import com.example.bemyplant.module.PlantModule
import io.realm.Realm
import io.realm.RealmConfiguration


class FlowerIdFragment() : Fragment() {
    private lateinit var plantImage: ImageView
    private lateinit var plantName: TextView
    private lateinit var plantRegistration: TextView
    private lateinit var realmPlant : Realm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_flower_id, container, false)
        val mainFlower = rootView.findViewById<ImageButton>(R.id.mainFlower)
        val plantRace = rootView.findViewById<TextView>(R.id.plantVariety)
        val plantName = rootView.findViewById<TextView>(R.id.flowerName)
        val plantRegistration = rootView.findViewById<TextView>(R.id.plantRegistration)

        val bundle = arguments
        Log.d(bundle.toString(), "test-bundle-id-fragment")
        //if (bundle != null && bundle.containsKey("plantImage") && bundle.containsKey("plantName") &&  bundle.containsKey("plantRace") && bundle.containsKey("plantRegistration")) {
//        if (bundle != null && bundle.containsKey("plantImageRes") && bundle.containsKey("plantName") &&  bundle.containsKey("plantRace") && bundle.containsKey("plantRegistration")) {
        if (bundle != null) {
            mainFlower.setImageBitmap(bundle.getParcelable("plantImage"))
            plantName.text = bundle.getString("plantName")
            plantRace.text = bundle.getString("plantRace")
            plantRegistration.text = bundle.getString("plantRegistration")
        }
        else{
            val configPlant : RealmConfiguration = RealmConfiguration.Builder()
                .name("plant.realm") // 생성할 realm 파일 이름 지정
                .deleteRealmIfMigrationNeeded()
                .modules(PlantModule())
                .allowWritesOnUiThread(true) // sdhan : UI thread에서 realm에 접근할수 있게 허용
                .build()
            realmPlant = Realm.getInstance(configPlant)

            var vo = realmPlant.where(PlantModel::class.java).findFirst()
            if (vo != null) {
                mainFlower.setImageBitmap(byteArrayToBitmap(vo.plantImage))
                plantName.text = vo.plantName
                plantRace.text = vo.plantRace
                plantRegistration.text = vo.plantRegNum
            }
            else{
                var emptyImg : Bitmap? = context?.let { ContextCompat.getDrawable(it, com.google.android.material.R.drawable.navigation_empty_icon)?.toBitmap() }
                mainFlower.setImageBitmap(emptyImg)
                plantName.text = "??"
                plantRace.text = "??"
                plantRegistration.text = "??"
            }
        }

//        mainFlower.setOnClickListener{
//            val flagValue = true
//            //val imageSelectFragment = ImageSelectFragment.newInstance(flagValue, plantName.text.toString(), plantRace.text.toString())
//            val imageSelectFragment = ImageSelectFragment.newInstance(flagValue, "", "")
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
//
//            sharedViewModel.imageText =  plantName.text.toString() + "을(를)\n다시 선택해보아요!"
//            transaction.replace(android.R.id.content, imageSelectFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
//
//        }

        return rootView
    }
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

}