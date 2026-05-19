package dev.wdona.flappybird.views

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import dev.wdona.flappybird.managers.SoundManager
import dev.wdona.flappybird.viewmodel.JuegoViewModel

class JuegoView(
    context: Context,
    private val alPerder: (Int) -> Unit
) : SurfaceView(context), SurfaceHolder.Callback {

    private val modeloJuego = JuegoViewModel()
    private var hiloJuego: HiloJuego? = null
    private var finJuegoActivado = false
    private var listo = false

    private val pinturaEsperaGrande = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 58f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
        setShadowLayer(6f, 3f, 3f, Color.argb(160, 0, 0, 0))
    }

    private val pinturaEsperaPequena = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 44f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
        setShadowLayer(4f, 2f, 2f, Color.argb(140, 0, 0, 0))
    }

    init {
        holder.addCallback(this)
        isFocusable = true
        setOnTouchListener { _, evento ->
            if (evento.action == MotionEvent.ACTION_DOWN) onSaltar()
            true
        }
        registrarListeners()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceChanged(holder: SurfaceHolder, formato: Int, ancho: Int, alto: Int) {
        if (!listo) {
            listo = true
            finJuegoActivado = false
            modeloJuego.inicializar(ancho.toFloat(), alto.toFloat())
        }
        if (hiloJuego == null) {
            hiloJuego = HiloJuego(holder).also { it.start() }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        detenerHilo()
    }

    fun onSaltar() = modeloJuego.saltar()

    fun onLuzCambiada(lux: Float) = modeloJuego.cambiarModoOscuro(lux < 20f)

    fun resume() {}

    fun pausar() = detenerHilo()

    private fun registrarListeners() {
        modeloJuego.onPerderListener = { puntuacion ->
            if (!finJuegoActivado) {
                finJuegoActivado = true
                SoundManager.reproducirSonido("colision")
                post { alPerder(puntuacion) }
            }
        }
        modeloJuego.onPuntuarListener = { SoundManager.reproducirSonido("punto") }
        modeloJuego.onSaltarListener  = { SoundManager.reproducirSonido("salto") }
    }

    private fun detenerHilo() {
        hiloJuego?.detener()
        hiloJuego = null
    }

    private inner class HiloJuego(private val portador: SurfaceHolder) : Thread() {
        @Volatile private var ejecutando = true
        private val duracionFotograma = 1000L / 60

        override fun run() {
            while (ejecutando) {
                val inicio = System.currentTimeMillis()
                modeloJuego.actualizar()
                val lienzo = portador.lockCanvas() ?: continue
                try {
                    dibujarFotograma(lienzo)
                } finally {
                    portador.unlockCanvasAndPost(lienzo)
                }
                val espera = duracionFotograma - (System.currentTimeMillis() - inicio)
                if (espera > 0) sleep(espera)
            }
        }

        fun detener() {
            ejecutando = false
            try { join(500) } catch (_: InterruptedException) {}
        }
    }

    private fun dibujarFotograma(lienzo: Canvas) {
        val anchoPantalla = lienzo.width.toFloat()
        val altoPantalla = lienzo.height.toFloat()
        dibujarEscenario(lienzo)
        dibujarObstaculos(lienzo)
        dibujarPajaro(lienzo)
        dibujarMarcador(lienzo, anchoPantalla)
        dibujarMensajeEspera(lienzo, anchoPantalla, altoPantalla)
    }

    private fun dibujarEscenario(lienzo: Canvas) {
        modeloJuego.escenario.dibujar(lienzo)
    }

    private fun dibujarObstaculos(lienzo: Canvas) {
        for (obstaculo in modeloJuego.obstaculos) {
            obstaculo.dibujar(lienzo, modeloJuego.modoOscuro)
        }
    }

    private fun dibujarPajaro(lienzo: Canvas) {
        modeloJuego.pajaro.dibujar(lienzo, modeloJuego.modoOscuro)
    }

    private fun dibujarMarcador(lienzo: Canvas, anchoPantalla: Float) {
        modeloJuego.hud.dibujar(lienzo, anchoPantalla)
    }

    private fun dibujarMensajeEspera(lienzo: Canvas, anchoPantalla: Float, altoPantalla: Float) {
        if (modeloJuego.estado == JuegoViewModel.Estado.ESPERANDO) {
            lienzo.drawText("Toca para empezar", anchoPantalla / 2, altoPantalla * 0.65f, pinturaEsperaGrande)
            lienzo.drawText("o agita el móvil", anchoPantalla / 2, altoPantalla * 0.73f, pinturaEsperaPequena)
        }
    }
}
