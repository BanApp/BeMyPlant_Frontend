package com.example.bemyplant.data

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

data class ChatMsg(
    var content : String,
    var senderID : String,
    var sendTime : String,
    var sendImage : Bitmap?
)
