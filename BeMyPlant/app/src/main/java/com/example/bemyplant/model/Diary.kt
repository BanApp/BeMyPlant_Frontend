package com.example.bemyplant.model

import android.os.Build
import androidx.annotation.RequiresApi
import io.realm.Realm
import io.realm.RealmObject
import java.time.LocalDate

//Diary model class
open class Diary : RealmObject() {

    lateinit var Date : String
    //lateinit var ID : String
    lateinit var Title : String
    var WeatherCode : Int = 0
    lateinit var Content : String
    lateinit var Image : ByteArray
    lateinit var Cr_Date: String
    lateinit var UP_Date: String

    /* @PrimaryKey
    var Date : String = "" //게시글 날짜 (ex- "2023/09/01" %04d/%02d/%02d  val date = "${day.year}/${String.format("%02d", day.month)}/${String.format("%02d", day.day)}")
    var ID : String = ""//회원 ID
    var Title : String = "" //게시글 제목
    var Content : String = "" //게시글 본문
    var Image : ByteArray? = null //이미지 파일 (BLOB)
    var Cr_Date: String = ""//게시글 작성일
    var UP_Date: String = ""//게시글 최종 수정일*/

    override fun toString(): String {
        return "title:" + Title + '\n' + "WeatherCode" + WeatherCode+ "cre_date" + Cr_Date + "up_date"+ UP_Date + "content:" + Content
    }

}

class DiaryRealmManager(val realm: Realm) {
    /*fun hasData(date: String): Diary? {
        return realm.where(Diary::class.java).equalTo("name", date).findFirst()
    }*/
    fun find(date: String): Diary? {
        return realm.where(Diary::class.java).equalTo("Date", date).findFirst()
    }
    fun findAll(): List<Diary> {
        return realm.where(Diary::class.java).findAll()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun create(curdata: Diary) {
        realm.beginTransaction()

        val data = realm.createObject(Diary::class.java)
        data.Date = curdata.Date
        //data.ID = curdata.ID
        data.Title = curdata.Title
        data.WeatherCode = curdata.WeatherCode
        data.Content = curdata.Content
        data.Image = curdata.Image

        data.Cr_Date = LocalDate.now().toString()
        data.UP_Date = data.Cr_Date

        realm.commitTransaction()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun update(date: String, curdata: Diary) {
        realm.beginTransaction()
        val data = find(date)
        data?.Date = curdata.Date
        //data?.ID = curdata.ID
        data?.Title = curdata.Title
        data?.WeatherCode = curdata.WeatherCode
        data?.Content = curdata.Content
        data?.Image = curdata.Image

        data?.UP_Date = LocalDate.now().toString()

        realm.commitTransaction()
    }

    fun deleteByDate(date: String) {
        realm.beginTransaction()
        val data = realm.where(Diary::class.java).equalTo("Date", date).findAll()
        data.deleteAllFromRealm()
        realm.commitTransaction()
    }

    fun deleteAll() {
        realm.beginTransaction()
        realm.where(Diary::class.java).findAll().deleteAllFromRealm()
        realm.commitTransaction()
    }

}