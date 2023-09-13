package com.example.sentryturretcontrolcenter

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class JoystickView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var cx = 0f
    private var cy = 0f
    private var r = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        cx = w / 2f
        cy = h / 2f
        r = Math.min(w, h) / 3f
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - cx
                val dy = event.y - cy
                val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
                if (distance < r) {
                    // Wewnątrz joysticka - przesuń go
                    cx = event.x
                    cy = event.y
                } else {
                    // Poza joystickiem - przesuń go na krawędź
                    val ratio = r / distance
                    cx += dx * (ratio - 1)
                    cy += dy * (ratio - 1)
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                // Przywróć joystick do środka
                cx = width / 2f
                cy = height / 2f
                invalidate()
            }
        }
        return true
    }
}