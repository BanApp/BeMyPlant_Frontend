package com.example.bemyplant

import android.os.Parcel
import android.os.Parcelable

class Day : Parcelable {
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var isInMonth = false


    // Parcelable 인터페이스 구현 부분
    // Parcelable을 구현하여 해당 클래스의 객체를 Bundle에 담을 수 있도록 함
    // 캘린더 클릭 시 다이어리로 화면이 전환되는데, 이때 날짜 정보(Day 정보)도 같이 넘기기 위해서 사용
    constructor(parcel: Parcel) {
        year = parcel.readInt()
        month = parcel.readInt()
        day = parcel.readInt()
        isInMonth = parcel.readByte() != 0.toByte()
    }
    constructor() {
        year = 0
        month = 0
        day = 0
        isInMonth = false
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(year)
        parcel.writeInt(month)
        parcel.writeInt(day)
        parcel.writeByte(if (isInMonth) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Day> {
        override fun createFromParcel(parcel: Parcel): Day {
            return Day(parcel)
        }

        override fun newArray(size: Int): Array<Day?> {
            return arrayOfNulls(size)
        }
    }
}
