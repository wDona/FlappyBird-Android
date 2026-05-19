package dev.wdona.flappybird

import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.wdona.flappybird.managers.AcelerometroManager
import dev.wdona.flappybird.managers.LuxManager
import dev.wdona.flappybird.managers.SoundManager
import dev.wdona.flappybird.views.GameOverView
import dev.wdona.flappybird.views.JuegoView
import dev.wdona.flappybird.views.MenuView

class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private var vistaJuego: JuegoView? = null

    override fun onCreate(estadoGuardado: Bundle?) {
        super.onCreate(estadoGuardado)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        SoundManager.inicializar(this)
        mostrarMenu()
    }

    private fun mostrarMenu() {
        vistaJuego = null
        setContentView(MenuView(this) { mostrarJuego() })
    }

    private fun mostrarJuego() {
        val juego = JuegoView(this) { puntuacion -> mostrarFinJuego(puntuacion) }
        vistaJuego = juego
        AcelerometroManager.registrar(sensorManager) { juego.onSaltar() }
        LuxManager.registrar(sensorManager) { lux -> juego.onLuzCambiada(lux) }
        setContentView(juego)
    }

    private fun mostrarFinJuego(puntuacion: Int) {
        AcelerometroManager.desregistrar(sensorManager)
        LuxManager.desregistrar(sensorManager)

        vistaJuego = null
        setContentView(GameOverView(this, puntuacion) { mostrarMenu() })
    }

    override fun onResume() {
        super.onResume()
        vistaJuego?.resume()
    }

    override fun onPause() {
        super.onPause()
        vistaJuego?.pausar()
        AcelerometroManager.desregistrar(sensorManager)
        LuxManager.desregistrar(sensorManager)
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundManager.liberar()
    }
}
