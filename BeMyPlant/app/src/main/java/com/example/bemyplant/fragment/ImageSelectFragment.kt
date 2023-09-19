package com.example.bemyplant.fragment

import android.os.Bundle
import android.content.Intent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.MainActivity
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentImageSelectBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageSelectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageSelectFragment : Fragment() {
    var selectedImage: String = ""

    val binding by lazy{FragmentImageSelectBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 이미지 선택하면 문자열이 넘어감.
        binding.imageButtonImageSelectPlant1.setOnClickListener{
            selectedImage = "flower1"
            val intent = Intent(requireActivity(), MainActivity::class.java)
            navigateToMain()
        }
        binding.imageButtonImageSelectPlant2.setOnClickListener{
            selectedImage = "flower2"
            val intent = Intent(requireActivity(), MainActivity::class.java)
            navigateToMain()
        }
    }
    // 다른 화면으로 값이 넘어가게끔
    private fun navigateToMain(){
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.putExtra("selectedImage", selectedImage)
        requireActivity().startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding.buttonImageSelectFinishButton.setOnClickListener {
//                                    val intent = Intent(requireActivity(), MainActivity::class.java)
//                        requireActivity().startActivity(intent)
            findNavController().navigate(R.id.action_iSFragment2_to_sRFragment)
        }// Inflate the layout for this fragment
        return binding.root
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
        @JvmStatic fun newInstance(param1: String, param2: String) =
            ImageSelectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

//package com.example.bemyplant.fragment
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.navigation.fragment.findNavController
//import com.example.bemyplant.R
//import com.example.bemyplant.databinding.FragmentISBinding
//import com.example.bemyplant.databinding.FragmentImageSelectBinding
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [ImageSelectFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class ImageSelectFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//    val binding by lazy{FragmentImageSelectBinding.inflate(layoutInflater)}
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        binding.button1.setOnClickListener {
//            findNavController().navigate(R.id.action_iSFragment2_to_sRFragment)
//        }// Inflate the layout for this fragment
//        return binding.root
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment imageSelectFragment.
//         */
//        @JvmStatic fun newInstance(param1: String, param2: String) =
//                ImageSelectFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}