package com.example.sentryturretcontrolcenter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import io.github.controlwear.virtual.joystick.android.JoystickView
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer

class LandscapeActivity : BaseActivity() {

    private var isConnected:String = "Disconnected" //Wartość domyślna
    private var mediaPlayer: MediaPlayer? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landscape)


        val surfaceView = findViewById<SurfaceView>(R.id.surfaceView)
        val libVLC = LibVLC(this, listOf("--no-drop-late-frames", "--no-skip-frames", "--rtsp-tcp").toMutableList())
        mediaPlayer = MediaPlayer(libVLC)

        val media = Media(libVLC, Uri.parse("rtsp://192.168.4.100:554"))
        mediaPlayer?.media = media
        media.release()

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                mediaPlayer?.setMedia(media)
                mediaPlayer?.setVideoTrackEnabled(true)
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                // Obsłuż zmiany powierzchni tutaj
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                // Obsłuż zniszczenie powierzchni tutaj
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            }
        })

        mediaPlayer?.play()


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

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
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
