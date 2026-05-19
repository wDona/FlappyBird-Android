package dev.wdona.flappybird.model

import android.graphics.*

class HUD {

    var puntuacion = 0

    private val pinturaPuntuacion = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 120f
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD

        setShadowLayer(8f, 4f, 4f, Color.argb(180, 0, 0, 0))
    }

    private val pinturaEtiqueta = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 48f
        color = Color.argb(200, 255, 255, 255)
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD

        setShadowLayer(4f, 2f, 2f, Color.argb(150, 0, 0, 0))
    }

    fun dibujar(lienzo: Canvas, anchoPantalla: Float) {
        lienzo.drawText(puntuacion.toString(), anchoPantalla / 2, 180f, pinturaPuntuacion)
    }

    fun dibujarFinJuego(lienzo: Canvas, anchoPantalla: Float, altoPantalla: Float, mejorPuntuacion: Int) {
        lienzo.drawText(puntuacion.toString(), anchoPantalla / 2, altoPantalla * 0.42f, pinturaPuntuacion)
        lienzo.drawText("Mejor: $mejorPuntuacion", anchoPantalla / 2, altoPantalla * 0.52f, pinturaEtiqueta)
    }
}
