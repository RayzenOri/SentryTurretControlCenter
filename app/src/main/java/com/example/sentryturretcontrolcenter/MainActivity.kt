package com.example.sentryturretcontrolcenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.sentryturretcontrolcenter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent

        //Odczytanie przekazanego statusu połączenia
        binding.amTextStatus.text = intent.getStringExtra("connectionStatus")

    }
    fun disconnectFromDevice(view: View){
        //Symulacja próby rozłączenia
        //Przejście do aktywności połączeniowej
        ConnectionManager.isConnected = false
        val intent = Intent(this,ConnectActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun goToSettings(view: View){
        val intent = Intent(this, OptionsActivity::class.java).apply {
            putExtra("connectionStatus",  binding.amTextStatus.text.toString())
        }
        startActivity(intent)
        finish()
    }
}