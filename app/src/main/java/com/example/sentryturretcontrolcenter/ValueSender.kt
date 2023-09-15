package com.example.sentryturretcontrolcenter

import NotificationSystem
import android.content.Context
import android.widget.Toast
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

object ValueSender {
    private val executor = Executors.newSingleThreadExecutor()

    fun sendValue(context: Context, value: String) {
        executor.execute {
            try {
                val url = URL("http://192.168.4.1/command?cmd=set_value=$value")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode

                val notificationSystem = NotificationSystem(context)

                if (responseCode == HttpURLConnection.HTTP_OK) { // Successfully sent the value to ESP32
                    notificationSystem.showToast("Value sent successfully") // Wyświetlenie komunikatu
                } else {
                    notificationSystem.showToast("Failed to send value") // Wyświetlenie komunikatu
                }

                connection.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
                val notificationSystem = NotificationSystem(context)
                notificationSystem.showToast("Error: ${e.message}")
            }
        }
    }
}
