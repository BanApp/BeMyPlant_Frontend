package com.example.bemyplant

//import kotlinx.android.synthetic.main.activity_main.* // Import your layout elements here
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.util.UUID

class testActivity : AppCompatActivity() {

    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private lateinit var mPairedDevices: Set<BluetoothDevice>
    private lateinit var mListPairedDevices: List<String>

    private lateinit var mBluetoothHandler: Handler
    private var mThreadConnectedBluetooth: ConnectedBluetoothThread? = null
    private lateinit var mBluetoothDevice: BluetoothDevice
    private lateinit var mBluetoothSocket: BluetoothSocket

    private val BT_REQUEST_ENABLE = 1
    private val BT_MESSAGE_READ = 2
    private val BT_CONNECTING_STATUS = 3
    private val BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")


    private lateinit var btnBluetoothOn : Button
    private lateinit var btnBluetoothOff : Button
    private lateinit var btnConnect : Button
    private lateinit var btnSendData : Button
    private lateinit var tvSendData1 : EditText
    private lateinit var tvSendData2 : EditText
    private lateinit var tvReceiveData : TextView
    private lateinit var tvBluetoothStatus : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test)

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()


        btnBluetoothOn = findViewById<Button>(R.id.btnBluetoothOn)
        btnBluetoothOff = findViewById<Button>(R.id.btnBluetoothOff)
        btnConnect = findViewById<Button>(R.id.btnConnect)
        btnSendData = findViewById<Button>(R.id.btnSendData)
        tvSendData1 = findViewById(R.id.tvSendData1)
        tvSendData2 = findViewById(R.id.tvSendData2)
        tvReceiveData = findViewById(R.id.tvReceiveData)
        tvBluetoothStatus = findViewById(R.id.tvBluetoothStatus)


        btnBluetoothOn.setOnClickListener {
            bluetoothOn()
        }

        btnBluetoothOff.setOnClickListener {
            bluetoothOff()
        }

        btnConnect.setOnClickListener {
            listPairedDevices()
        }


        btnSendData.setOnClickListener {
            if (mThreadConnectedBluetooth != null) {
                val ssid = tvSendData1.text.toString()
                val password =tvSendData2.text.toString()
                val json = createJson(ssid, password)
                mThreadConnectedBluetooth?.write(json.toString())

                //tvSendData.text = ""
            }
        }

        mBluetoothHandler = object : Handler() {
            override fun handleMessage(msg: android.os.Message) {
                if (msg.what == BT_MESSAGE_READ) {
                    val readMessage: String?
                    try {
                        readMessage = String(msg.obj as ByteArray, charset("UTF-8"))
                        tvReceiveData.text = readMessage
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun createJson(ssid: String, password: String): JSONObject {
        val json = JSONObject()
        json.put("ssid", ssid)
        json.put("password", password)
        return json
    }

    private fun bluetoothOn() {
        if (mBluetoothAdapter == null) {
            // 블루투스를 지원하지 않는 기기
            showToast("블루투스를 지원하지 않는 기기입니다.")
        } else {
            if (mBluetoothAdapter.isEnabled) {
                // 블루투스가 이미 활성화되어 있음
                showToast("블루투스가 이미 활성화되어 있습니다.")
                tvBluetoothStatus.text = "활성화"
            } else {
                // 블루투스를 활성화
                showToast("블루투스를 활성화합니다.")
                val intentBluetoothEnable = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE)
            }
        }
    }

    private fun bluetoothOff() {
        if (mBluetoothAdapter.isEnabled) {
            // 블루투스 비활성화
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mBluetoothAdapter.disable()
            showToast("블루투스가 비활성화되었습니다.")
            tvBluetoothStatus.text = "비활성화"
        } else {
            showToast("블루투스가 이미 비활성화되어 있습니다.")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            BT_REQUEST_ENABLE -> {
                if (resultCode == RESULT_OK) {
                    showToast("블루투스 활성화")
                    tvBluetoothStatus.text = "활성화"
                } else if (resultCode == RESULT_CANCELED) {
                    showToast("취소")
                    tvBluetoothStatus.text = "비활성화"
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun listPairedDevices() {
        showToast("listPairedDevices함수 실행")
        if (mBluetoothAdapter.isEnabled) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                showToast("권한 설정 실패")
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mPairedDevices = mBluetoothAdapter.bondedDevices

            if (mPairedDevices.size > 0) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("장치 선택")

                mListPairedDevices = ArrayList()
                for (device in mPairedDevices) {
                    (mListPairedDevices as ArrayList<String>).add(device.name)
                }
                val items = mListPairedDevices.toTypedArray()

                builder.setItems(items) { dialog, item ->
                    connectSelectedDevice(items[item].toString())
                }
                val alert = builder.create()
                alert.show()
            } else {
                showToast("페어링된 장치가 없습니다.")
            }
        } else {
            showToast("블루투스가 비활성화되어 있습니다.")
        }
    }

    private fun connectSelectedDevice(selectedDeviceName: String) {
        for (tempDevice in mPairedDevices) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            if (selectedDeviceName == tempDevice.name) {
                mBluetoothDevice = tempDevice
                break
            }
        }
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID)
            mBluetoothSocket.connect()
            mThreadConnectedBluetooth = ConnectedBluetoothThread(mBluetoothSocket)
            mThreadConnectedBluetooth?.start()
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget()
        } catch (e: IOException) {
            showToast("블루투스 연결 중 오류가 발생했습니다.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private inner class ConnectedBluetoothThread(socket: BluetoothSocket) : Thread() {
        private val mmSocket: BluetoothSocket = socket
        private val mmInStream: InputStream?
        private val mmOutStream: OutputStream?

        init {
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null
            try {
                tmpIn = socket.inputStream
                tmpOut = socket.outputStream
            } catch (e: IOException) {
                showToast("소켓 연결 중 오류가 발생했습니다.")
            }
            mmInStream = tmpIn
            mmOutStream = tmpOut
        }

        override fun run() {
            val buffer = ByteArray(1024)
            var bytes: Int

            while (true) {
                try {
                    bytes = mmInStream!!.available()
                    if (bytes != 0) {
                        SystemClock.sleep(100)
                        bytes = mmInStream.read(buffer, 0, bytes)
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
                showToast("데이터 전송 중 오류가 발생했습니다.")
            }
        }

        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                showToast("소켓 해제 중 오류가 발생했습니다.")
            }
        }
    }
}
