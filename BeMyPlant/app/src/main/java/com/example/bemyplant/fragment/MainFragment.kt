package com.example.bemyplant.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    val binding by lazy{FragmentMainBinding.inflate((layoutInflater))}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        return binding.root
    }
}