package com.example.bemyplant.fragment

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentS1Binding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [s1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class s1Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val binding by lazy{FragmentS1Binding.inflate(layoutInflater)}
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
        binding.button1.setOnClickListener {
            /*사용자이름 및 핸드폰 번호*/
            val r_name = binding.editText1.text.toString()
            val phones = binding.editText2.text.toString()
            val bundle = bundleOf("r_name" to r_name, "phones" to phones)
            if (r_name == "" ){
                showToast(requireContext(),"사용자 이름을 입력해주세요")
            } else if (phones== ""){
                showToast(requireContext(),"핸드폰 번호를 입력해주세요")
            } else {
            findNavController().navigate(R.id.action_s1Fragment_to_s2Fragment,bundle)

            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignFragment1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            s1Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}