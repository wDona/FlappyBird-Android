package dev.wdona.flappybird.views

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import dev.wdona.flappybird.model.Escenario

class MenuView(context: Context, private val alIniciar: () -> Unit) : View(context) {

    private val escenario = Escenario()

    private val fondo = Paint().apply {
        color = Color.argb(120, 0, 0, 80)
    }

    private val titulo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 130f
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
        setShadowLayer(12f, 6f, 6f, Color.argb(200, 0, 0, 0))
    }

    private val subtitulo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 50f
        color = Color.rgb(255, 240, 100)
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
        setShadowLayer(6f, 3f, 3f, Color.argb(150, 0, 0, 0))
    }

    override fun onSizeChanged(ancho: Int, alto: Int, anchoAnterior: Int, altoAnterior: Int) {
        escenario.inicializar(ancho.toFloat(), alto.toFloat())
    }

    override fun onDraw(lienzo: Canvas) {
        val anchoPantalla = lienzo.width.toFloat()
        val altoPantalla = lienzo.height.toFloat()

        escenario.dibujar(lienzo)
        lienzo.drawRect(0f, 0f, anchoPantalla, altoPantalla, fondo)

        lienzo.drawText("Flappy", anchoPantalla / 2, altoPantalla * 0.30f, titulo)
        lienzo.drawText("Bird", anchoPantalla / 2, altoPantalla * 0.43f, titulo)
        lienzo.drawText("Agita o toca para jugar", anchoPantalla / 2, altoPantalla * 0.55f, subtitulo)
    }

    override fun onTouchEvent(evento: MotionEvent): Boolean {
        if (evento.action == MotionEvent.ACTION_DOWN) alIniciar()
        return true
    }
}
