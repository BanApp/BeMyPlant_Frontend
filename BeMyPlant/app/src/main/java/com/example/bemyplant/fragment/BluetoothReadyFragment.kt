package com.example.bemyplant.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.MainActivity
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentBluetoothReadyBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BluetoothReadyFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentBluetoothReadyBinding

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
        binding = FragmentBluetoothReadyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString("username").toString()
        val bundle = bundleOf(
            "username" to username
        )
        binding.plantButton.setOnClickListener{
            println("♥♥♥♥♥♥♥♥♥♥♥")
            println(username)
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.putExtra("username", username)
            requireActivity().startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BluetoothReadyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
