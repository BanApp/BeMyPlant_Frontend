package com.example.bemyplant.fragment

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.ConnectedBluetoothThread
import com.example.bemyplant.MainActivity
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentBluetoothConnectBinding
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.util.UUID

class BluetoothConnectFragment : Fragment() {
    val binding by lazy{ FragmentBluetoothConnectBinding.inflate(layoutInflater)}
    var mThreadConnectedBluetooth: ConnectedBluetoothThread? = null
    private val REQUEST_BLUETOOTH_PERMISSION = 123 // 블루투스 요청 권한 요청

    private lateinit var mBluetoothDevice: BluetoothDevice
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private lateinit var mPairedDevices: Set<BluetoothDevice>
    private lateinit var mListPairedDevices: List<String>
    private lateinit var mBluetoothHandler: Handler
    private lateinit var mBluetoothSocket: BluetoothSocket

    private val BT_REQUEST_ENABLE = 1
    // private val BT_MESSAGE_READ = 2
    private val BT_CONNECTING_STATUS = 3
    private val BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var bluetoothConnect = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.skipButton.setOnClickListener {
            // TODO: 확인버튼 삭제할 것 ! (자동 이동)
            findNavController().navigate(R.id.action_bCFragment2_to_bSFragment2)

            val intent = Intent(requireActivity(), MainActivity::class.java)
            requireActivity().startActivity(intent)
        }

//        ConnectedBluetoothThread.mBluetoothHandler = object : Handler() {
//            override fun handleMessage(msg: android.os.Message) {
//                if (msg.what == ConnectedBluetoothThread.BT_MESSAGE_READ) {
//                    val readMessage: String?
//                    try {
//                        readMessage = String(msg.obj as ByteArray, charset("UTF-8"))
//                        //tvReceiveData.text = readMessage
//                    } catch (e: UnsupportedEncodingException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        // 블루투스 권한 부여 요청
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 허용되지 않은 경우 권한 요청을 수행
            requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_BLUETOOTH_PERMISSION)
        }

        // 블루투스 활성화
        bluetoothOn()

        // 페어링된 블루투스 장치 선택
        listPairedDevices()
    }

    private fun bluetoothOn() {
        if (mBluetoothAdapter == null) {
            showToast("블루투스를 지원하지 않는 기기입니다.")
        } else {
            if (!mBluetoothAdapter.isEnabled) {
                // 블루투스를 활성화
                val intentBluetoothEnable = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE)
                showToast("블루투스 활성화 성공")
            }
        }
    }

    private fun listPairedDevices() {
        Log.d("test-listPairedDevices", "listPariedDevices 함수 실행")

        if (mBluetoothAdapter.isEnabled) {
            Log.d("test-listPairedDevices", "mBluetoothAdapter is Enabled")

            mPairedDevices = mBluetoothAdapter.bondedDevices

            if (mPairedDevices.size > 0) {
                //foundDevice = true
                Log.d("test-listPairedDevices", "mPairedDevices.size > 0")
                val builder = AlertDialog.Builder(requireContext())
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
                //foundDevice = false
                Log.d("test-listPairedDevices", "페어링된 장치가 없습니다")
                //scanDevice()
            }
        } else {
            showToast("블루투스가 비활성화되어 있습니다.")
            Log.d("test-listPairedDevices", "블루투스가 비활성화되어 있습니다")
        }
    }

    private fun connectSelectedDevice(selectedDeviceName: String) {
        for (tempDevice in mPairedDevices) {
            if (selectedDeviceName == tempDevice.name) {
                mBluetoothDevice = tempDevice
                break
            }
        }
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID)
            Log.d("bluetooth", "mBluetoothSocket complete")
            //showToast("bluetooth: mBluetoothSocket complete")

            try {
                mBluetoothSocket.connect()
                bluetoothConnect = true
                Log.d("bluetooth", "mBluetoothSocket connect")
                //showToast("bluetooth: mBluetoothSocket connect")

            } catch (e: IOException) {
                // 연결 시도 중 예외 발생 시 해당 에러 메시지를 출력
                Log.e("BluetoothConnection", "Connection error: ${e.message}")
                //showToast("BluetoothConnection: Connection error: ${e.message}")
                e.printStackTrace()
            }

            mBluetoothHandler = object : Handler() {
                override fun handleMessage(msg: android.os.Message) {
                    if (msg.what == ConnectedBluetoothThread.BT_MESSAGE_READ) {
                        val readMessage: String?
                        try {
                            readMessage = String(msg.obj as ByteArray, charset("UTF-8"))
                            //tvReceiveData.text = readMessage
                        } catch (e: UnsupportedEncodingException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            mThreadConnectedBluetooth = ConnectedBluetoothThread.initialize(mBluetoothSocket, mBluetoothHandler)
            Log.d("bluetooth", "mThreadConnectedBluetooth")
            //showToast("bluetooth: mThreadConnectedBluetooth")

            mThreadConnectedBluetooth?.start()
            Log.d("bluetooth", "mThreadConnectedBluetooth?.start()")
            //showToast("bluetooth: mThreadConnectedBluetooth?.start()")

            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget()
            Log.d("bluetooth", "obtainMessage")
            showToast("블루투스 연결 성공")


            // 다음 화면으로 이동
            findNavController().navigate(R.id.bSFragment2)
            // findNavController().navigate(action_bCFragment2_to_bSFragment2)


        } catch (e: IOException) {
            showToast("블루투스 연결 중 오류가 발생했습니다.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            BluetoothConnectFragment().apply {}
    }
}