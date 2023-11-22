package com.example.bemyplant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.ConnectedBluetoothThread
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentBluetoothSend1Binding
import org.json.JSONObject

class BluetoothSend1Fragment : Fragment() {
    val binding by lazy{FragmentBluetoothSend1Binding.inflate(layoutInflater)}
    private var wifiEdit: String? = null
    private var wifiPwEdit: String? = null
    private var userIdEdit: String? = null
    private var userPwEdit: String? = null
    val mThreadConnectedBluetooth = ConnectedBluetoothThread

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*wifiEdit = binding.wifiEdit.text.toString()
        wifiPwEdit = binding.wifiPwEdit.text.toString()
        userIdEdit = binding.userIdEdit.text.toString()
        userPwEdit = binding.userPwEdit.text.toString()
*/


        binding.finishButton.setOnClickListener {
            wifiEdit = binding.editTextWifiSsid.text.toString()
            wifiPwEdit = binding.editTextWifiPwd.text.toString()

            userIdEdit = binding.editTextUserId.text.toString()
            userPwEdit = binding.editTextUserPwd.text.toString()
            // 보내기 시도
            if (mThreadConnectedBluetooth != null) {
                val json = createJson(wifiEdit.toString(), wifiPwEdit.toString(), userIdEdit.toString(), userPwEdit.toString() )
                mThreadConnectedBluetooth?.write(json.toString())
                showToast("전송 성공")
                // 만일 보내기 성공 시 화면이동
                // findNavController().navigate(R.id.action_bSFragment2_to_bS2Fragment2)
                findNavController().navigate(R.id.action_bSFragment2_to_bS2Fragment2)
            }
        }

    }

    private fun createJson(wifi_ssid: String, wifi_password: String, user_id: String, user_password: String): JSONObject {
        val json = JSONObject()
        json.put("ssid", wifi_ssid)
        json.put("password", wifi_password)
        json.put("user_id", user_id)
        json.put("user_pw", user_password)
        return json
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BluetoothSend1Fragment().apply {}
    }
}