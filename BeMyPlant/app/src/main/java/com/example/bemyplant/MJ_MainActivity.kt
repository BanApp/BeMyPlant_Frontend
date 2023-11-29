package com.example.bemyplant


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MJ_MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mj_main)
    }
//주석
//    fun replaceFragment(fragment: Fragment?) {
//        Log.d("test", "replace Fragment function come ....");
//        val fragmentManager: FragmentManager = supportFragmentManager
//        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//        if (fragment != null) {
//            fragmentTransaction.replace(R.id.mainFragment2, fragment)
//                .commit()
//        } // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
//    }

}