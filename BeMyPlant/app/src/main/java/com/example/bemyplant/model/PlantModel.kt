package com.example.bemyplant.model

import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.RealmClass


@RealmClass
open class PlantModel: RealmObject() {
    lateinit var plantName : String          // 식물 이름
    lateinit var plantBirth : String           // 식물 생일
    lateinit var plantRace : String         // 식물 품종
    lateinit var plantImage : ByteArray      // 식물 이미지 파일(BLOB)
    lateinit var userImage : ByteArray      // 식물 이미지 파일(BLOB)
    lateinit var plantRegNum : String     // 식물 주민 등록번호
}

//open class PlantModel(
//    var plantName : String,          // 식물 이름
//    var plantBirth : String,           // 식물 생일
//    var plantRace : String,       // 식물 품종
//    var plantImage : ByteArray,      // 식물 이미지 파일(BLOB)
//    var userImage : ByteArray,      // 식물 이미지 파일(BLOB)
//    var plantRegNum : String        // 식물 주민 등록번호
//
//): RealmObject()
