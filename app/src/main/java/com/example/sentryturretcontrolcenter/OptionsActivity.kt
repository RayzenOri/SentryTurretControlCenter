package com.example.sentryturretcontrolcenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import java.util.concurrent.Executors
import com.example.sentryturretcontrolcenter.databinding.ActivityOptionsBinding
import okhttp3.*
import java.io.IOException

class OptionsActivity : BaseActivity() {

    private lateinit var binding: ActivityOptionsBinding
    private var isConnected:String = "Disconnected" //Wartość domyślna
    private val executor = Executors.newSingleThreadExecutor() //pozwala na wykonywanie operacji w tle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        binding = ActivityOptionsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val btSend = binding.aoBtSendData
        val switch = binding.switchPatrolMode
        btSend.setOnClickListener { sendData() }
        switch.setOnCheckedChangeListener { _, _ -> sendData() }

        val intent = intent

        //Odczytanie przekazanego statusu połączenia
        isConnected = intent.getStringExtra("connectionStatus").toString()
        Log.d("Connection Status:", isConnected)

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
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                // Odczytaj odpowiedź i zapisz do zmiennej
                val responseData = response.body?.string()
                Log.d("MyApp", "Odebrana wartość: $responseData")

                // Parsowanie odpowiedzi
                val lines = responseData?.split("\n")
                lines?.forEach { line ->
                    val parts = line.split("=")
                    when (parts[0]) {
                        "get_rotationSpeed_x" -> runOnUiThread { binding.editRotationX.hint = parts[1] }
                        "get_rotationSpeed_y" -> runOnUiThread { binding.editRotationY.hint = parts[1] }
                        "get_precision_x" -> runOnUiThread { binding.editPrecisionX.hint = parts[1] }
                        "get_precision_y" -> runOnUiThread { binding.editPrecisionY.hint = parts[1] }
                        "get_returnTime" -> runOnUiThread { binding.editReturnTime.hint = (parts[1].toInt()/1000).toString() }
                        "get_patrolMode" -> runOnUiThread { binding.switchPatrolMode.isChecked = parts[1].toIntOrNull() ?: 0 != 0 }
                    }
                }
            }
        })
    }

    fun sendData(){
        var value = "set_rotation_speed_x=" + if(""==binding.editRotationX.text.toString()) 0
        else binding.editRotationX.text.toString() + "_y=" + if(""==binding.editRotationY.text.toString()) 0
        else binding.editRotationY.text.toString()
        ValueSender.sendValue(this,value)
        Log.d("TAG",value)

        //Pole Precyzji
        value = "set_precision_x=" + if(""==binding.editPrecisionX.text.toString()) 0
        else binding.editPrecisionX.text.toString() + "_y=" + if(""==binding.editPrecisionY.text.toString()) 0
        else binding.editPrecisionY.text.toString()
        ValueSender.sendValue(this,value)
        Log.d("TAG",value)

        //Czas powrotu
        var returnTime = if(""==binding.editReturnTime.text.toString()) 0
        else binding.editReturnTime.text.toString().toInt()
        if(returnTime > 60) returnTime = 60
        value = "set_return_time=$returnTime"
        ValueSender.sendValue(this,value)
        Log.d("TAG",value)

        //Tryb Patrolowania
        value = "set_mode=" + if(binding.switchPatrolMode.isChecked) "on" else "off"
        ValueSender.sendValue(this,value)
        Log.d("TAG",value)
    }


    fun goBackToMainActivity(view: View){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("connectionStatus", isConnected)
        }
        startActivity(intent)
        finish()
    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("connectionStatus", isConnected)
        }
        startActivity(intent)
        finish()
    }

}