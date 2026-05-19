package dev.wdona.flappybird.model

import android.graphics.*
import dev.wdona.flappybird.interfaces.Dibujable

class Obstaculo(anchoPantalla: Float, private val altoPantalla: Float) : Dibujable {

    val ancho = 140f
    private val velocidadDesplazamiento = 6f
    private val alturaHueco = 380f

    var x = anchoPantalla + ancho
    private val centroHueco: Float
    var superado = false

    private val pinturaCaneria = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        val margen = 220f
        val rango = (altoPantalla - margen * 2 - alturaHueco).coerceAtLeast(10f)
        centroHueco = margen + (Math.random() * rango).toFloat() + alturaHueco / 2
    }

    override fun actualizarPosicion() {
        x -= velocidadDesplazamiento
    }

    override fun redibujar() {}

    override fun desaparecer() {
        x = -ancho * 2
    }

    fun isFueraDePantalla() = x + ancho < 0

    fun colisionCon(limitesP: RectF): Boolean {
        return RectF.intersects(obtenerRectCaneriaArriba(), limitesP) ||
               RectF.intersects(obtenerRectCaneriaAbajo(), limitesP)
    }

    fun dibujar(lienzo: Canvas, modoOscuro: Boolean) {
        pinturaCaneria.color = if (modoOscuro) Color.rgb(30, 120, 30) else Color.rgb(50, 180, 50)
        dibujarCaneriaArriba(lienzo)
        dibujarCaneriaAbajo(lienzo)
    }

    private fun obtenerRectCaneriaArriba(): RectF {
        return RectF(x, 0f, x + ancho, centroHueco - alturaHueco / 2)
    }

    private fun obtenerRectCaneriaAbajo(): RectF {
        return RectF(x, centroHueco + alturaHueco / 2, x + ancho, altoPantalla)
    }

    private fun dibujarCaneriaArriba(lienzo: Canvas) {
        lienzo.drawRect(obtenerRectCaneriaArriba(), pinturaCaneria)
    }

    private fun dibujarCaneriaAbajo(lienzo: Canvas) {
        lienzo.drawRect(obtenerRectCaneriaAbajo(), pinturaCaneria)
    }
}
