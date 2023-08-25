package com.example.sentryturretcontrolcenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class ConnectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)


    }
    fun connectToDevice(view: View){
        //Symulacja próby połączenia
        val ipAddress = "192.168.4.1"
        ConnectionManager.ipAddress = ipAddress
        val isConnectedText = findViewById<TextView>(R.id.ac_textStatus)


        if(checkConnection(ipAddress)){
            isConnectedText.text = getString(R.string.StatusConnected);
            Thread.sleep(2_000)

            ConnectionManager.isConnected = true

            //Przejście do aktywności głównej
            val intent = Intent(this,MainActivity::class.java).apply {
                putExtra("connectionStatus",isConnectedText.text.toString())
            }
            startActivity(intent)
            finish()
        }

    }
    private fun checkConnection(ipAddress: String): Boolean {
        return ConnectionChecker.checkConnectionAsync(ipAddress)
    }
}