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
 * Use the [SignUp1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUp1Fragment : Fragment() {
    val binding by lazy{FragmentS1Binding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}