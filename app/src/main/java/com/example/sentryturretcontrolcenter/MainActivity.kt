package com.example.sentryturretcontrolcenter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.sentryturretcontrolcenter.databinding.ActivityMainBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import okhttp3.*
import kotlin.properties.Delegates

class MainActivity : BaseActivity() {

    private var autoMode: Boolean by Delegates.observable(false) { _, _, newValue ->
        binding.btAuto.setBackgroundColor(if (newValue) Color.GREEN else Color.RED)
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent

        binding.btAuto.setOnClickListener { autoMode() }

        // Utwórz klienta OkHttp
        val client = OkHttpClient()

        // Utwórz żądanie do serwera ESP32
        val request = Request.Builder()
            .url("http://192.168.4.1:80/sendData")
            .build()

        // Wyślij żądanie i zarejestruj funkcję zwrotną do obsługi odpowiedzi
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                //if (!response.isSuccessful) throw IOException("Unexpected code $response")

                // Odczytaj odpowiedź i zapisz do zmiennej
                val responseData = response.body?.string()
                Log.d("MyApp", "Odebrana wartość: $responseData")

                // Parsowanie odpowiedzi
                val lines = responseData?.split("\n")
                lines?.forEach { line ->
                    val parts = line.split("=")
                    when (parts[0]) {
                        "get_autoMode" -> autoMode = "1" == parts[1]
                    }
                }
            }
        })

        //Odczytanie przekazanego statusu połączenia
        binding.amTextStatus.text = intent.getStringExtra("connectionStatus")

    }
    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("Sentry Turret Control Center", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("currentActivity", javaClass.simpleName).apply()
    }
    fun disconnectFromDevice(view: View){
        //Przejście do aktywności połączeniowej
        val intent = Intent(this,ConnectActivity::class.java)
        startActivity(intent)
        finish()

    }
    private fun autoMode(){
        autoMode = !autoMode
        val value = if (autoMode) "autoMode=on" else "autoMode=off"
        ValueSender.sendValue(this, value)
        Log.d("AutoMode",value)
    }
    fun goToSettings(view: View){
        val value = "options_opened"
        ValueSender.sendValue(this, value)
        Log.d("Options",value)
        val intent = Intent(this, OptionsActivity::class.java).apply {
            putExtra("connectionStatus",  binding.amTextStatus.text.toString())
        }
        startActivity(intent)
        finish()
    }

    fun goToManualControl(view: View){
        val value = "manual_opened"
        ValueSender.sendValue(this, value)
        val intent = Intent(this, LandscapeActivity::class.java).apply {
            putExtra("connectionStatus",  binding.amTextStatus.text.toString())
        }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        //Przejście do aktywności połączeniowej
        val intent = Intent(this,ConnectActivity::class.java)
        startActivity(intent)
        finish()
    }
}