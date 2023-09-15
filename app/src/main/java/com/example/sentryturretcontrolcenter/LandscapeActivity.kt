package com.example.sentryturretcontrolcenter

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import io.github.controlwear.virtual.joystick.android.JoystickView

class LandscapeActivity : BaseActivity() {

    private var isConnected:String = "Disconnected" //Wartość domyślna

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landscape)

        //Odczytanie przekazanego statusu połączenia
        isConnected = intent.getStringExtra("connectionStatus").toString()

        val joystick = findViewById<JoystickView>(R.id.joystickView)
        joystick.setOnMoveListener { angle, strength ->
            // tutaj możesz zrobić co chcesz z wartościami kąta i siły
            //Log.d("Angle",angle.toString())

            //Stop
            if(angle==0){
                Log.d("Stop", "Stop")
                var value = "set_rotation_x=off_y=off"
                //ValueSender.sendValue(this,value)

            }
            //Prawo
            else if((angle>337 || angle<22) && angle>0){
                Log.d("Kierunek","PRAWO")
                var value = "set_rotation_x=right_y=off"
                //ValueSender.sendValue(this,value)
            }
            else if(angle in 23..66){
                Log.d("Kierunek","PRAWY-GÓRNY")
                var value = "set_rotation_x=right_y=up"
                //ValueSender.sendValue(this,value)
            }
            else if(angle in 67..112){
                Log.d("Kierunek","GÓRA")
                var value = "set_rotation_x=off_y=up"
                //ValueSender.sendValue(this,value)
            }
            else if(angle in 113..157){
                Log.d("Kierunek","LEWY-GÓRNY")
                var value = "set_rotation_x=left_y=up"
                //ValueSender.sendValue(this,value)
            }
            else if(angle in 158..202){
                Log.d("Kierunek","LEWY")
                var value = "set_rotation_x=left_y=off"
                //ValueSender.sendValue(this,value)
            }
            else if(angle in 203 .. 247){
                Log.d("Kierunek","LEWY-DOLNY")
                var value = "set_rotation_x=left_y=down"
                //ValueSender.sendValue(this,value)
            }
            else if(angle in 248 .. 292){
                Log.d("Kierunek","DÓŁ")
                var value = "set_rotation_x=off_y=down"
                //ValueSender.sendValue(this,value)
            }
            else if(angle in 293 .. 337){
                Log.d("Kierunek","PRAWY-DOLNY")
                var value = "set_rotation_x=right_y=down"
                //ValueSender.sendValue(this,value)
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
