package com.example.bemyplant.data

import android.graphics.Bitmap

data class ChatMsg(
    val content : String,
    val senderID : String,
    val sendTime : String,
    val image: Bitmap
)
