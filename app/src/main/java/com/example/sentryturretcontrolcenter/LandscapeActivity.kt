package com.example.sentryturretcontrolcenter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import io.github.controlwear.virtual.joystick.android.JoystickView
import android.webkit.WebView
import android.webkit.WebViewClient


class LandscapeActivity : BaseActivity() {

    private var isConnected:String = "Disconnected" //Wartość domyślna


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landscape)

        window.insetsController?.let {
            it.hide(WindowInsets.Type.statusBars())
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val mWebView = findViewById<WebView>(R.id.webview)
        mWebView.webViewClient = WebViewClient() // Ustawienie WebViewClient pozwala na otwieranie linków wewnątrz WebView zamiast w domyślnej przeglądarce.

        val webSettings = mWebView.settings
        webSettings.javaScriptEnabled = true // Włączenie JavaScriptu jest konieczne do poprawnego wyświetlania strumienia MJPEG.

        mWebView.loadUrl("http://192.168.4.100:81/stream") // Załaduj URL strumienia MJPEG.




        //Odczytanie przekazanego statusu połączenia
        isConnected = intent.getStringExtra("connectionStatus").toString()

        val joystick = findViewById<JoystickView>(R.id.joystickView)
        joystick.setOnMoveListener { angle, strength ->
            // tutaj możesz zrobić co chcesz z wartościami kąta i siły
            //Log.d("Angle",angle.toString())


            if(strength in 0..50){
                Log.d("Stop", "Stop") //Stop
                var value = "set_rotation_x=off_y=off"
                ValueSender.sendValue(this,value)
            }else{
                when(angle){
                    in 337..360, in 0..22-> { //PRAWO
                        Log.d("Kierunek","PRAWO")
                        var value = "set_rotation_x=right_y=off"
                        ValueSender.sendValue(this,value)
                    }
                    in 23..66->{
                        Log.d("Kierunek","PRAWY-GÓRNY") //PRAWY-GÓRNY
                        var value = "set_rotation_x=right_y=up"
                        ValueSender.sendValue(this,value)
                    }
                    in 67..112->{
                        Log.d("Kierunek","GÓRA") //GÓRA
                        var value = "set_rotation_x=off_y=up"
                        ValueSender.sendValue(this,value)
                    }
                    in 113..157->{
                        Log.d("Kierunek","LEWY-GÓRNY") //LEWY-GÓRNY
                        var value = "set_rotation_x=left_y=up"
                        ValueSender.sendValue(this,value)
                    }
                    in 158..202->{
                        Log.d("Kierunek","LEWY") //LEWY
                        var value = "set_rotation_x=left_y=off"
                        ValueSender.sendValue(this,value)
                    }
                    in 203 .. 247->{
                        Log.d("Kierunek","LEWY-DOLNY") //LEWY-DOLNY
                        var value = "set_rotation_x=left_y=down"
                        ValueSender.sendValue(this,value)
                    }
                    in 248 .. 292->{
                        Log.d("Kierunek","DÓŁ") //DÓŁ
                        var value = "set_rotation_x=off_y=down"
                        ValueSender.sendValue(this,value)
                    }
                    in 293 .. 337->{
                        Log.d("Kierunek","PRAWY-DOLNY") //PRAWY-DOLNY
                        var value = "set_rotation_x=right_y=down"
                        ValueSender.sendValue(this,value)
                    }
                }
            }
        }

    }

    fun fire(view: View){
        val value = "fire"
        ValueSender.sendValue(this,value);
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
