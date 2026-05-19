package dev.wdona.flappybird.managers

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

object LuxManager {

    private var sensorListener: SensorEventListener? = null
    private val UMBRAL_OSCURIDAD = 20f

    fun registrar(sensorManager: SensorManager, onLuzCambiadaListener: (Float) -> Unit) {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) ?: return

        sensorListener = object : SensorEventListener {
            override fun onSensorChanged(evento: SensorEvent) {
                onLuzCambiadaListener(evento.values[0])
            }

            override fun onAccuracyChanged(sensor: Sensor?, exactitud: Int) {}
        }

        sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun desregistrar(gestorSensores: SensorManager) {
        sensorListener?.let { gestorSensores.unregisterListener(it) }
        sensorListener = null
    }

    fun isOscuro(lux: Float) = lux < UMBRAL_OSCURIDAD
}
