package dev.wdona.flappybird.managers

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

object AcelerometroManager {

    private var sensorListener: SensorEventListener? = null
    private var onAgitarListener: (() -> Unit)? = null
    private val UMBRAL_AGITACION = 14f
    private var ultimaAgitacion = 0L

    fun registrar(sensorManager: SensorManager, listener: () -> Unit) {
        onAgitarListener = listener
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ?: return

        sensorListener = object : SensorEventListener {
            override fun onSensorChanged(evento: SensorEvent) {
                if (isAgitacion(evento)) notificarAgitacion()
            }

            override fun onAccuracyChanged(sensor: Sensor?, exactitud: Int) {}
        }

        sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_GAME)
    }

    fun desregistrar(sensorManager: SensorManager) {
        sensorListener?.let { sensorManager.unregisterListener(it) }
        sensorListener = null
        onAgitarListener = null
    }

    fun onAgitar() = onAgitarListener?.invoke()

    private fun isAgitacion(evento: SensorEvent): Boolean {
        val ax = evento.values[0]
        val ay = evento.values[1]
        val az = evento.values[2]

        val aceleracion = sqrt(ax * ax + ay * ay + az * az) - SensorManager.GRAVITY_EARTH
        val ahora = System.currentTimeMillis()

        return aceleracion > UMBRAL_AGITACION && ahora - ultimaAgitacion > 350
    }

    private fun notificarAgitacion() {
        ultimaAgitacion = System.currentTimeMillis()
        onAgitarListener?.invoke()
    }
}
