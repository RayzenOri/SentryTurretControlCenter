package com.example.sentryturretcontrolcenter

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.IBinder

class WifiCheckService : Service() {

    private val handler = Handler()
    private val runnableCode = object : Runnable {
        override fun run() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

            if (networkCapabilities == null || !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                val sharedPreferences = getSharedPreferences("Sentry Turret Control Center", Context.MODE_PRIVATE)
                val currentActivity = sharedPreferences.getString("currentActivity", "")
                if (currentActivity != "ConnectActivity") {
                    val intent = Intent(this@WifiCheckService, ConnectActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }

            handler.postDelayed(this, 5000)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.post(runnableCode)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnableCode)
    }
}
