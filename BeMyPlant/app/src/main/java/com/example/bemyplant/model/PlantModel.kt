package com.example.bemyplant.model

import io.realm.RealmList
import io.realm.RealmObject
import org.bson.types.Binary

open class PlantModel: RealmObject() {
    lateinit var P_Name : String          // 식물 이름
    lateinit var P_Birth : String           // 식물 생일
    lateinit var P_Race : String         // 식물 품종
    lateinit var P_Image : ByteArray      // 식물 이미지 파일(BLOB)
    lateinit var P_Registration : String     // 식물 주민 등록번호

//    var P_Name : String? = ""          // 식물 이름
//    var P_Birth : String? = ""
//    var P_Race : String? = ""          // 식물 품종
//    lateinit var P_Image : ByteArray      // 식물 이미지 파일(BLOB)
//    var P_Registration : String? = ""    // 식물 주민 등록번호

//    var P_Name : String = ""           // 식물 이름
//    var P_Birth : String = ""        // 식물 생일
//    var P_Race : String = ""          // 식물 품종
//    var P_Image : ByteArray = byteArrayOf(0)      // 식물 이미지 파일(BLOB)
//    var P_Registration : String = ""    // 식물 주민 등록번호
}