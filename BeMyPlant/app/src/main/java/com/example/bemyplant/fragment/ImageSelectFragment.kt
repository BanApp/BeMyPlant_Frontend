package com.example.bemyplant.fragment

import android.os.Bundle
import android.content.Intent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.MainActivity
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentImageSelectBinding
import androidx.lifecycle.ViewModel
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import android.widget.Button


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SharedViewModel : ViewModel() {
    var imageText: String = ""
}
class ImageSelectFragment : Fragment() {
    val binding by lazy{ FragmentImageSelectBinding.inflate(layoutInflater)}
    var newPlantImageResId: Int = 0
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var flag: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            flag = it.getBoolean("flag", false)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_image_select, container, false)
        val flowerButton1 = rootView.findViewById<ImageButton>(R.id.flowerButton1)
        val flowerButton2 = rootView.findViewById<ImageButton>(R.id.flowerButton2)
        val finishButton = rootView.findViewById<Button>(R.id.finishButton)
        val changeText = rootView.findViewById<TextView>(R.id.imageText)
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
            intent.putExtra("newPlantImageResId", newPlantImageResId)
            //--------flag값에 따라 화면 전환
            if (flag) {
                startActivity(intent)
            } else  {
                findNavController().navigate(R.id.action_iSFragment2_to_sRFragment)
            }
        }


        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment imageSelectFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String,flag: Boolean) =
            ImageSelectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putBoolean("flag", flag)
                }
            }
    }
}
