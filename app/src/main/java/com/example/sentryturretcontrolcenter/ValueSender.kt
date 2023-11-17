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

    val excluded = listOf("fire","manual_closed","options_opened","manual_opened")

    fun sendValue(context: Context, value: String) {
        executor.execute {
            try {
                val url = URL("http://192.168.4.1/command?cmd=set_value=$value")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode

                val notificationSystem = NotificationSystem(context)

                if (excluded.none{it == value} && !value.contains("set_rotation_x=")) {
                    val message = if (responseCode == HttpURLConnection.HTTP_OK)  context.getString(R.string.vs_Success) else context.getString(R.string.vs_failed)
                    (context as Activity).runOnUiThread {
                        notificationSystem.showToast(message)
                    }
                }

                connection.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
                (context as Activity).runOnUiThread {
                    val notificationSystem = NotificationSystem(context)
                    notificationSystem.showToast(R.string.vs_error.toString()+" ${e.message}")
                }
            }
        }
    }
}

