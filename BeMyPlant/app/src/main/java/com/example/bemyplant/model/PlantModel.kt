package com.example.bemyplant.model

import io.realm.RealmObject

open class PlantModel: RealmObject() {
    var P_Name : String = ""           // 식물 이름
    var P_Birth : String = ""          // 식물 생일
    var P_Race : String = ""          // 식물 품종
//    var P_Image : ByteArray        // 식물 이미지 파일(BLOB)
    var P_Registration : String = ""    // 식물 주민 등록번호

//    lateinit var P_Name : String           // 식물 이름
//    lateinit var P_Birth : String          // 식물 생일
//    lateinit var P_Race : String          // 식물 품종
//    //    var P_Image : ByteArray        // 식물 이미지 파일(BLOB)
//    lateinit var P_Registration : String    // 식물 주민 등록번호
}