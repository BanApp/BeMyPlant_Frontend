package com.example.bemyplant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentImageSelectBinding

class SettingDeleteAccountPopupFragment: Fragment() {
    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_delete_account_popup, container, false)
    }
}