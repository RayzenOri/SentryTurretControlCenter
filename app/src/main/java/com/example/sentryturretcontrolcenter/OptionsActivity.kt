package com.example.sentryturretcontrolcenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import java.util.concurrent.Executors
import com.example.sentryturretcontrolcenter.databinding.ActivityOptionsBinding


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

    fun sendRotationSpeed(view: View){ //I tak do zmiany ale na razie musi tak być, potem dodam odczyt z esp
        val value = "set_rotation_speed_x=" + if(""==binding.editRotationX.text.toString()) "0"
        else binding.editRotationX.text.toString() + "_y=" + if(""==binding.editRotationY.text.toString()) 0
        else binding.editRotationY.text.toString()
        ValueSender.sendValue(this,value)
        Log.d("TAG",value)
    }
    fun sendPrecision(view: View){
        val value = "set_precision_x=" + binding.editPrecisionX.text.toString() +
                "_y=" + binding.editPrecisionY.text.toString()
        //ValueSender.sendValue(this,value)
        Log.d("TAG",value)
    }
    fun sendReturnTime(view: View){
        var value = "set_return_time=" + binding.editReturnTime.text.toString()
        if(value.toInt()>60) value = "60"
        ValueSender.sendValue(this,value)
        Log.d("TAG",value)
    }
    fun sendMode(view: View){
        val value = "set_mode=" + if(binding.switchPatrolMode.isChecked) "on" else "off"
        //ValueSender.sendValue(this,value)
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