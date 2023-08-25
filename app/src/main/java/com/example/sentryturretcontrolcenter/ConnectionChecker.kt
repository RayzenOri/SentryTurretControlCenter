package com.example.sentryturretcontrolcenter

import java.net.InetAddress

object ConnectionChecker {

    private var isConnected = false

    fun isConnected(): Boolean {
        return isConnected
    }

    fun checkConnectionAsync(ipAddress: String): Boolean {
        var isConnectedNow = false

        try {
            val address = InetAddress.getByName(ipAddress)
            isConnectedNow = address.isReachable(3000)
        } catch (e: Exception) {
            e.printStackTrace()
            isConnectedNow = false
        }

        isConnected = isConnectedNow
        return isConnectedNow
    }
}
