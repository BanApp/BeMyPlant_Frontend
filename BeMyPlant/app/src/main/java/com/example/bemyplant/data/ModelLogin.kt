/*
package com.example.bemyplant.data

import com.google.gson.annotations.SerializedName

// 날씨 정보를 담는 데이터 클래스
data class ModelLogin (
    @SerializedName("login_id") var login_id: String = "",      // 아이디
    @SerializedName("login_pw") var login_pw: String = "",      // 비밀번호

)

// xml 파일 형식을 data class로 구현
data class WEATHER (val response : RESPONSE)
data class RESPONSE(val header : HEADER, val body : BODY)
data class HEADER(val resultCode : Int, val resultMsg : String)
data class BODY(val dataType : String, val items : ITEMS, val totalCount : Int)
data class ITEMS(val item : List<ITEM>)

// category : 자료 구분 코드, fcstDate : 예측 날짜, fcstTime : 예측 시간, fcstValue : 예보 값
data class ITEM(val category : String, val fcstDate : String, val fcstTime : String, val fcstValue : String)*/
