package com.example.bemyplant.model

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class UserModel: RealmObject() {
    lateinit var userName : String
    lateinit var userImage : ByteArray      // 식물 이미지 파일(BLOB)
}