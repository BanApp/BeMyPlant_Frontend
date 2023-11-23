package com.example.bemyplant

import android.app.Application
import io.realm.Realm

class App : Application() {
    override fun onCreate() {
        super.onCreate()
//        Realm.init(this);
        // Initialize Realm. Should only be done once when the application starts.
        //val realmConfig : RealmConfiguration = RealmConfiguration.Builder().build();
        // DiaryRealmManager 인스턴스 초기화
       //Realm.init(this);
    }
}