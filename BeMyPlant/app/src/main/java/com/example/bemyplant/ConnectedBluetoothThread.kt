package com.example.bemyplant

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream



object ConnectedBluetoothThread : Thread() {
    private lateinit var mmSocket: BluetoothSocket
    private var mmInStream: InputStream? = null
    private var mmOutStream: OutputStream? = null
    val BT_MESSAGE_READ = 2
    lateinit var mBluetoothHandler:Handler

    fun initialize(socket: BluetoothSocket, mBluetoothHandler: Handler):ConnectedBluetoothThread {
        this.mmSocket = socket
        var tmpIn: InputStream? = null
        var tmpOut: OutputStream? = null
        try {
            tmpIn = socket.inputStream
            tmpOut = socket.outputStream
        } catch (e: IOException) {
            Log.d("connectedBluetoothThread", "소켓 연결 중 오류가 발생했습니다.")
        }
        mmInStream = tmpIn
        mmOutStream = tmpOut
        this.mBluetoothHandler = mBluetoothHandler
        return this
    }

    override fun run() {
        val buffer = ByteArray(1024)
        var bytes: Int

        while (true) {
            try {
                bytes = mmInStream!!.available()
                if (bytes != 0) {
                    SystemClock.sleep(100)
                    bytes = mmInStream!!.read(buffer, 0, bytes)
                    mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget()
                }
            } catch (e: IOException) {
                break
            }
        }
    }

    fun write(str: String) {
        val bytes = str.toByteArray()
        try {
            mmOutStream?.write(bytes)
        } catch (e: IOException) {
            Log.d("connectedBluetoothThread", "데이터 전송 중 오류가 발생했습니다.")
        }
    }

    fun cancel() {
        try {
            mmSocket.close()
        } catch (e: IOException) {
            Log.d("connectedBluetoothThread","소켓 해제 중 오류가 발생했습니다.")
        }
    }

}