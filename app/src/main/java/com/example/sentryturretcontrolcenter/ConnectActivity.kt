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
        val isConnected = findViewById<TextView>(R.id.ac_textStatus)
        if(isConnected.text.toString() == getString(R.string.StatusDisconnected)){
            isConnected.text = getString(R.string.StatusConnected);
            Thread.sleep(2_000)
            //Przejście do aktywności głównej
            val intent = Intent(this,MainActivity::class.java).apply {
                putExtra("connectionStatus",isConnected.text.toString())
            }
            startActivity(intent)
            finish()
        }

    }
}