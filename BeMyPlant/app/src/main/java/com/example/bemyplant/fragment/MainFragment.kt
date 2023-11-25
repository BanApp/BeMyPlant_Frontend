package com.example.bemyplant.fragment

import android.os.Bundle
import android.text.Spannable
import androidx.core.content.ContextCompat
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentMainBinding
import com.example.bemyplant.model.PlantModel
import com.example.bemyplant.module.PlantModule
import io.realm.Realm
import io.realm.RealmConfiguration

class MainFragment : Fragment() {
    val binding by lazy{FragmentMainBinding.inflate((layoutInflater))}
    private lateinit var realm : Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val configPlant : RealmConfiguration = RealmConfiguration.Builder()
            .name("appdb.realm") // 생성할 realm 파일 이름 지정
            .deleteRealmIfMigrationNeeded()
            .modules(PlantModule())
            .allowWritesOnUiThread(true) // sdhan : UI thread에서 realm에 접근할수 있게 허용
            .build()
        realm = Realm.getInstance(configPlant)

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