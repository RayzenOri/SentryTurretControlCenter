package com.example.sentryturretcontrolcenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.sentryturretcontrolcenter.databinding.ActivityMainBinding
import com.example.sentryturretcontrolcenter.databinding.ActivityOptionsBinding

class OptionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOptionsBinding
    private var isConnected:String = "Disconnected" //Wartość domyślna

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
    fun goBackToMainActivity(view: View){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("connectionStatus", isConnected)
        }
        startActivity(intent)
        finish()
    }
}