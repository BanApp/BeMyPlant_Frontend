package com.example.bemyplant.popup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.bemyplant.MainActivity
import com.example.bemyplant.R

class DeletePlantPopupFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.fragment_setting_delete_plant_popup, container, false)
        val yesButton = rootview.findViewById<Button>(R.id.appCompatButton_deletePlant_yes)
        yesButton.setOnClickListener {
            // 이미지 변경
            val deletePlant = R.drawable.delete_plant
            val bundle = Bundle()
            bundle.putInt("newPlantImageResId", deletePlant)

            // 화면 이동
            val mainActivityIntent = Intent(requireContext(), MainActivity::class.java)
            mainActivityIntent.putExtras(bundle)
            startActivity(mainActivityIntent)
        }
        return rootview
    }

    private fun navigateToMainActivity() {
        val mainActivityIntent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(mainActivityIntent)
    }
}