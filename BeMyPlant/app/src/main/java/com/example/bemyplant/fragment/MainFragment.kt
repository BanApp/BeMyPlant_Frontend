package com.example.bemyplant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        // Inflate the layout for this fragment
        binding.loginButton.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment2_to_loginFragment3)
        }
        binding.signButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment2_to_s1Fragment)
        }
        return binding.root
    }

}