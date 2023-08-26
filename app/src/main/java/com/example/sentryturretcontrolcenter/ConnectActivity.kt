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
import android.widget.TextView

class ConnectActivity : BaseActivity() {
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
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifiManager.isWifiEnabled) {
            // Wi-Fi jest włączone
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()
            val networkCallback = object : ConnectivityManager.NetworkCallback(ConnectivityManager.NetworkCallback.FLAG_INCLUDE_LOCATION_INFO) {
                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    val wifiInfo = networkCapabilities.transportInfo as WifiInfo
                    val ssid = wifiInfo.ssid
                    if (ssid == "\"ESP32\"") {
                        // Telefon jest podłączony do sieci ESP32
                        // Symulacja próby połączenia
                        runOnUiThread {
                            // Kod zmieniający widok
                            val isConnected = findViewById<TextView>(R.id.ac_textStatus)
                            isConnected.text = getString(R.string.StatusConnected)
                            // Przejście do aktywności głównej
                            val intent = Intent(this@ConnectActivity, MainActivity::class.java).apply {
                                putExtra("connectionStatus", isConnected.text.toString())
                            }
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        // Telefon nie jest podłączony do sieci ESP32, przełącz użytkownika do ustawień Wi-Fi
                        val intent = Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)
                        startActivity(intent)
                    }
                }
            }
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        } else {
            // Wi-Fi jest wyłączone, przełącz użytkownika do ustawień Wi-Fi
            val intent = Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)
            startActivity(intent)
        }
    }


}

