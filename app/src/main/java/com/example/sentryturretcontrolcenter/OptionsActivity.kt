package com.example.sentryturretcontrolcenter

import NotificationSystem
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import java.util.concurrent.Executors
import com.example.sentryturretcontrolcenter.databinding.ActivityOptionsBinding
import java.net.HttpURLConnection
import java.net.URL
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

        val intent = intent

        //Odczytanie przekazanego statusu połączenia
        isConnected = intent.getStringExtra("connectionStatus").toString()
        Log.d("Connection Status:", isConnected)
    }

    fun sendRotation(view: View){
        val value = "set_rotation_x=" + binding.editRotationX.text.toString() +
                "_y=" + binding.editRotationY.text.toString()
        //sendValue(value)
        Log.d("TAG",value)
    }
    fun sendPrecision(view: View){
        val value = "set_precision_x=" + binding.editPrecisionX.text.toString() +
                "_y=" + binding.editPrecisionY.text.toString()
        //sendValue(value)
        Log.d("TAG",value)
    }
    fun sendReturnTime(view: View){
        val value = "set_return_time=" + binding.editReturnTime.text.toString()
        sendValue(value)
        //sendValue(value)
        Log.d("TAG",value)
    }
    fun sendMode(view: View){
        val value = "set_mode=" + if(binding.switchPatrolMode.isChecked) "on" else "off"
        //sendValue(value)
        Log.d("TAG",value)
    }

    private fun sendValue(value: String){
        executor.execute{
            try {
                val url = URL("http://192.168.4.1/command?cmd=set_value=$value")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                val notificationSystem = NotificationSystem(this)
                runOnUiThread{
                    if(responseCode == HttpURLConnection.HTTP_OK){ // Successfully sent the value to ESP32

                        notificationSystem.showToast(getString(R.string.notification_successSent)) // Wyświetlenie komunikatu
                        binding.editRotationX.hint = value

                    }else{
                        notificationSystem.showToast(getString(R.string.notification_failSent)) // Wyświetlenie komunikatu
                    }
                }

                connection.disconnect()
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
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