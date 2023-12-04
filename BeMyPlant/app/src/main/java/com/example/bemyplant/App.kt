package com.example.bemyplant

import android.app.Application
import android.content.Context
import android.content.Intent
import io.realm.Realm

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        val sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val token: String? = sharedPreferences.getString("token", null)

        if (token == null) {
            // token이 없을 때 처리
            val intent = Intent(this, MJ_MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }


        // Initialize Realm. Should only be done once when the application starts.
        //val realmConfig : RealmConfiguration = RealmConfiguration.Builder().build();
        // DiaryRealmManager 인스턴스 초기화
       //Realm.init(this);
    }
}