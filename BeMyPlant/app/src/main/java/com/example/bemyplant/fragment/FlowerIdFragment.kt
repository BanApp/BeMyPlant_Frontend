package com.example.bemyplant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bemyplant.R


class FlowerIdFragment(bundle: Bundle) : Fragment() {
    private lateinit var plantImage: ImageView
    private lateinit var plantName: TextView
    private lateinit var plantRegistration: TextView

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
        if (bundle != null && bundle.containsKey("plantImage") && bundle.containsKey("plantName") &&  bundle.containsKey("plantRace") && bundle.containsKey("plantRegistration")) {
            mainFlower.setImageBitmap(bundle.getParcelable("plantImage"))
            plantName.text = bundle.getString("plantName")
            plantRace.text = bundle.getString("plantRace")
            plantRegistration.text = bundle.getString("plantRegistration")
        }
        else{
            // TODO: (정현) 식물 DB 조회
            //      식물 이미지, 이름, 품종, 주민 등록번호 가져와서 렌더링 (mainFlower, flowerName, plantRace,  plantRegistration)

        }

        mainFlower.setOnClickListener{
            val flagValue = true
            //val imageSelectFragment = ImageSelectFragment.newInstance(flagValue, plantName.text.toString(), plantRace.text.toString())
            val imageSelectFragment = ImageSelectFragment.newInstance(flagValue, "", "")
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

            sharedViewModel.imageText =  plantName.text.toString() + "을(를)\n다시 선택해보아요!"
            transaction.replace(android.R.id.content, imageSelectFragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }

        return rootView
    }

}