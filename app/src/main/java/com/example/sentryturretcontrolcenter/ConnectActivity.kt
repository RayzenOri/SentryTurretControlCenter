package com.example.sentryturretcontrolcenter


import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class ConnectActivity : BaseActivity() {

    private var btPressed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.WRITE_SETTINGS), 0)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)


        }


    }
    fun connectToDevice(view: View) {
        btPressed = true
        onResume()
    }

    override fun onResume() {
        super.onResume()

        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        Log.d("WIFI ENABLED", wifiManager.isWifiEnabled.toString())

        if (wifiManager.isWifiEnabled) {
            val wifiInfo = wifiManager.connectionInfo
            val ssid = wifiInfo.ssid
            Log.d("SSID", ssid)

            if (ssid == "\"ESP32\"") {
                // Jesteś podłączony do sieci ESP32
                findViewById<Button>(R.id.btConnect).text = getString(R.string.ac_btContinue)
                findViewById<TextView>(R.id.ac_textStatus).text = getString(R.string.StatusConnectedToTurret)
                if (btPressed) {
                    btPressed = false
                    val intent = Intent(this@ConnectActivity, MainActivity::class.java).apply {
                        putExtra("connectionStatus", getString(R.string.StatusConnected).toString())
                    }
                    startActivity(intent)
                    finish()
                }
            } else {
                // Nie jesteś podłączony do sieci ESP32
                findViewById<Button>(R.id.btConnect).text = getString(R.string.ac_btConnect)
                findViewById<TextView>(R.id.ac_textStatus).text = getString(R.string.StatusDisconnectedFromTurret)
                if (btPressed) {
                    btPressed = false
                    val intent = Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)
                    startActivity(intent)
                }
            }
        } else {
            // WiFi jest wyłączone
            findViewById<TextView>(R.id.ac_textStatus).text = getString(R.string.StatusWiFiDisabled)
            findViewById<Button>(R.id.btConnect).text = getString(R.string.ac_btGoToSettings)
            if (btPressed) {
                btPressed = false
                val intent = Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)
                startActivity(intent)
            }
        }
    }


}

