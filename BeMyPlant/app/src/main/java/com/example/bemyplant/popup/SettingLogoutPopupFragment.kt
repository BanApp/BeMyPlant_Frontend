package com.example.bemyplant.popup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bemyplant.MainActivity
import com.example.bemyplant.R

class SettingLogoutPopupFragment: Fragment() {
    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootview = inflater.inflate(R.layout.fragment_setting_logout_popup, container, false)

        return rootview
    }

    private fun navigateToMainActivity() {
        val mainActivityIntent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(mainActivityIntent)
    }
}