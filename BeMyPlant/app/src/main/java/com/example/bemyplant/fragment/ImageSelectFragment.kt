package com.example.bemyplant.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.MainActivity
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentImageSelectBinding

class SharedViewModel : ViewModel() {
    var imageText: String = ""
}
class ImageSelectFragment : Fragment() {
    val binding by lazy{ FragmentImageSelectBinding.inflate(layoutInflater)}
    var newPlantImageResId: Int = 0
    private var flag: Boolean = false
    private var plantName: String = ""
    private var plantRace: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            flag = it.getBoolean("flag", false)
            plantName = it.getString("name", "")
            plantRace = it.getString("race", "")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_image_select, container, false)
        val flowerButton1 = rootView.findViewById<ImageButton>(R.id.imageButton_imageSelect_plant1)
        val flowerButton2 = rootView.findViewById<ImageButton>(R.id.imageButton_imageSelect_plant2)
        val finishButton = rootView.findViewById<Button>(R.id.button_imageSelect_finishButton)
        val changeText = rootView.findViewById<TextView>(R.id.textView_imageSelect_plantName)
        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        if (flag){
            changeText.text = sharedViewModel.imageText
        }

        flowerButton1.setOnClickListener {
            flowerButton1.setBackgroundResource(R.drawable.image_select)
            flowerButton2.setBackgroundResource(0)
            newPlantImageResId = R.drawable.flower
        }

        flowerButton2.setOnClickListener {
            flowerButton2.setBackgroundResource(R.drawable.image_select)
            flowerButton1.setBackgroundResource(0)
            newPlantImageResId = R.drawable.flower2
        }
        finishButton.setOnClickListener {
            //----메인엑티비티에 이미지 값 전달
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("newPlantImageResId", newPlantImageResId) // TODO: 실제 이미지값으로 변경
            //--------flag값에 따라 화면 전환
            if (flag) {
                // TODO: (정현) 식물 DB에서 식물 이미지 update (newPlantImageResId)
                startActivity(intent)
            } else  {
                // TODO: (정현) 식물 DB에서 식물 정보 create (plantName, plantRace, newPlantImageResId, Plant Birth plantRegistration)
                // 참고 - 현재 날짜를 구해 P_Birth 연산하고 DB에 넣을 것
                // 참고 - plantRegistration에서 P_Birth와 임의의 랜덤값을 이용해 식물 주민 등록번호를 생성할 것
                findNavController().navigate(R.id.action_iSFragment2_to_sRFragment)
            }
        }
        return rootView
    }

    companion object {
        @JvmStatic fun newInstance(flag: Boolean, plantName: String, plantRace: String) =
            ImageSelectFragment().apply {
                arguments = Bundle().apply {
                    putBoolean("flag", flag)
                    putString("name", plantName)
                    putString("race", plantRace)
                }
            }
    }
}
