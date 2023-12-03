package com.example.bemyplant.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentSensorRegisterBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SensorRegisterRFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SensorRegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val binding by lazy{FragmentSensorRegisterBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val username = arguments?.getString("username").toString()
        val bundle = bundleOf(
            "username" to username
        )
        binding.finishButton.setOnClickListener {
            println("♥♥♥♥♥♥♥♥♥♥♥")
            println(username)
            findNavController().navigate(R.id.action_sRFragment_to_bCFragment2, bundle)
        }
        // 버튼 클릭시, 각각 센서에 대한 설명 보이도록 구현
        binding.luminousButton.setOnClickListener {
            popupExplainFragment(R.layout.fragment_explain_sensor1)
        }
        binding.soilButton.setOnClickListener {
            popupExplainFragment(R.layout.fragment_explain_sensor2)
        }
        binding.temperatureButton.setOnClickListener {
            popupExplainFragment(R.layout.fragment_explain_sensor3)
        }
        return binding.root
        // Inflate the layout for this fragment
    }
    //센서 설명에 대한 팝업창 연결
    private fun popupExplainFragment(layoutResId: Int) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(layoutResId)
//        val layoutParams = ViewGroup.LayoutParams(850 , 600)
//        dialog.window?.setLayout(layoutParams.width, layoutParams.height)
        dialog.show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment sRFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SensorRegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}