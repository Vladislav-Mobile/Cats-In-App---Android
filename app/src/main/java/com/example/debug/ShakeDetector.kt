package com.example.catsinapp.debug

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(
    private val onShake: () -> Unit
) : SensorEventListener {

    private val shakeThreshold = 2.7f
    private val minTimeBetweenShakes = 1000L
    private var lastShakeTime = 0L

    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f
    private var lastUpdate = 0L

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val now = System.currentTimeMillis()
        if ((now - lastUpdate) < 100) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val deltaX = x - lastX
        val deltaY = y - lastY
        val deltaZ = z - lastZ

        val acceleration = sqrt(
            deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ
        ) / SensorManager.GRAVITY_EARTH

        if (acceleration > shakeThreshold) {
            val now2 = System.currentTimeMillis()
            if (now2 - lastShakeTime > minTimeBetweenShakes) {
                lastShakeTime = now2
                onShake()
            }
        }

        lastX = x; lastY = y; lastZ = z
        lastUpdate = now
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    companion object {
        fun register(context: Context, detector: ShakeDetector) {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            sensorManager.registerListener(
                detector,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI
            )
        }

        fun unregister(context: Context, detector: ShakeDetector) {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            sensorManager.unregisterListener(detector)
        }
    }
}
