package com.example.sentryturretcontrolcenter

import NotificationSystem
import android.app.Activity
import android.content.Context
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

                if (!value.contains("set_rotation_x=")) { // Dodana linia warunkowa
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        (context as Activity).runOnUiThread {
                            notificationSystem.showToast("Value sent successfully")
                        }
                    } else {
                        (context as Activity).runOnUiThread {
                            notificationSystem.showToast("Failed to send value")
                        }
                    }
                }

                connection.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
                (context as Activity).runOnUiThread {
                    val notificationSystem = NotificationSystem(context)
                    notificationSystem.showToast("Error: ${e.message}")
                }
            }
        }
    }
}

