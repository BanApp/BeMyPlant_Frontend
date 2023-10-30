package com.example.bemyplant.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
//import android.support.v4.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.bemyplant.R


//class PlantRegisterFragment() : Fragment(){
class PlantRegisterFragment() : Fragment() {
    private lateinit var navController: NavController
    lateinit var finishButton : Button
    lateinit var plantNameEditText: EditText
    lateinit var plantSearchView: SearchView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.example.bemyplant.R.layout.fragment_plant_register, container, false)

        return view
    }
    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        if (navController == null){
            Log.d("test", "... findNavController not success . ..")
            return
        }
        Log.d("test", "... findNavController success . ..")

        finishButton = view.findViewById(com.example.bemyplant.R.id.finishButton)
        plantNameEditText = view.findViewById(com.example.bemyplant.R.id.plantNameInput)
        plantSearchView = view.findViewById(com.example.bemyplant.R.id.plantSpeciesSearch)


        finishButton.setOnClickListener() {
            // 식물 이름 같이 보내기
            val bundle = Bundle()
            bundle.putString("name", plantNameEditText.text.toString())
            bundle.putString("race", plantSearchView.query.toString())


//            val navHostFragment =
//                parentFragmentManager.findFragmentById(com.example.bemyplant.R.id.nav_host_fragment1)
//            if (navHostFragment == null) {
//                Log.d("test", "cannot find navHostFragment")
//            }
//            val navController = navHostFragment.navController
//                navController.navigate(R.id.action_pRFragment_to_iSFragment2)

            //val navController = findNavController()
//            if (navController == null) {
//                Log.d("test", "...cannot find navController")
//            }


            Log.d("test", "... findNavController success . ..")
            navController.navigate(R.id.action_pRFragment_to_iSFragment2)
            Log.d("test", "... navController navigate . ..")


            //navController.navigate(R.id.iSFragment2)

            //navController = Navigation.findNavController(view)
//            if (navController == null) {
//                Log.d("test", "cannot find navController")
//            }
//            else{
//                navController.navigate(R.id.action_pRFragment_to_iSFragment2)
//            }


//            val navHostFragment = parentFragmentManager.findFragmentById(R.id.nav_host_fragment1)
//            if (navHostFragment is NavHostFragment) { //NavHostFragment
//                // NavHostFragment를 찾았을 때의 코드
//                // NavHostFragment를 사용하여 내비게이션을 관리합니다.
//                val navController = navHostFragment.navController
//                navController.navigate(R.id.action_pRFragment_to_iSFragment2)
//
//            } else {
//                // NavHostFragment를 찾지 못했을 때의 처리
//                Log.d("test", "cannot find NavHostFragment")
//            }


        //navController.navigate(R.id.iSFragment2, bundle)
            /*navController = binding.
                .getFragment<NavHostFragment>().navController*/

            //findNavController().navigate(R.id.action_pRFragment_to_iSFragment2)
            //navController.navigate(R.id.iSFragment2)
        }
        /*binding.finishButton.setOnClickListener {
            //findNavController().navigate(R.id.action_pRFragment_to_iSFragment2)
            findNavController().navigate(R.id.iSFragment2)
        }*/
    }

    companion object {
        fun newInstance(): Fragment? {
            val fragment = PlantRegisterFragment()
            return fragment;
        }
    }
}


