package com.example.bemyplant.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bemyplant.R
import android.widget.Button
import com.example.bemyplant.MainActivity
import android.content.Intent
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DeletePlantPopupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeletePlantPopupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val rootview = inflater.inflate(R.layout.fragment_delete_plant_popup, container, false)
        val yesButton = rootview.findViewById<Button>(R.id.yesButton)
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DeletePlantPopupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DeletePlantPopupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}