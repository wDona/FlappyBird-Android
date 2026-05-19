package dev.wdona.flappybird.model

import android.graphics.*
import dev.wdona.flappybird.interfaces.Dibujable

class Escenario : Dibujable {

    var modoOscuro = false
    private var desplazamiento = 0f
    private val velocidadDesplazamiento = 3f
    private var anchoPantalla = 0f
    private var altoPantalla = 0f

    private val pinturaCielo = Paint()
    private val pinturaSuelo = Paint()
    private val pinturaNube = Paint(Paint.ANTI_ALIAS_FLAG)

    fun inicializar(ancho: Float, alto: Float) {
        anchoPantalla = ancho
        altoPantalla = alto
    }

    override fun actualizarPosicion() {
        desplazamiento += velocidadDesplazamiento
        if (desplazamiento > anchoPantalla) desplazamiento -= anchoPantalla
    }

    override fun redibujar() {}

    override fun desaparecer() {}

    fun cambiarModo() {
        modoOscuro = !modoOscuro
    }

    fun dibujar(lienzo: Canvas) {
        if (modoOscuro) dibujarNoche(lienzo) else dibujarDia(lienzo)
    }

    private fun dibujarDia(lienzo: Canvas) {
        pinturaCielo.color = Color.rgb(135, 206, 235)
        lienzo.drawRect(0f, 0f, anchoPantalla, altoPantalla, pinturaCielo)

        pinturaNube.color = Color.WHITE
        dibujarNubes(lienzo)
        dibujarSuelo(lienzo, Color.rgb(100, 200, 80), Color.rgb(150, 110, 55))
    }

    private fun dibujarNoche(lienzo: Canvas) {
        pinturaCielo.color = Color.rgb(10, 10, 40)
        lienzo.drawRect(0f, 0f, anchoPantalla, altoPantalla, pinturaCielo)

        dibujarEstrellas(lienzo)
        dibujarSuelo(lienzo, Color.rgb(25, 75, 25), Color.rgb(50, 35, 15))
    }

    private fun dibujarNubes(lienzo: Canvas) {
        val posicionesNubes = listOf(0.1f to 0.1f, 0.5f to 0.07f, 0.85f to 0.13f, 1.2f to 0.09f)
        for ((rx, ry) in posicionesNubes) {
            val cx = ((anchoPantalla * rx - desplazamiento * 0.4f) % (anchoPantalla * 1.4f) + anchoPantalla * 1.4f) % (anchoPantalla * 1.4f)
            dibujarNube(lienzo, cx, altoPantalla * ry)
        }
    }

    private fun dibujarNube(lienzo: Canvas, cx: Float, cy: Float) {
        lienzo.drawOval(cx - 60f, cy - 25f, cx + 60f, cy + 25f, pinturaNube)
    }

    private val posicionesEstrellas = listOf(
        0.1f to 0.05f, 0.3f to 0.03f, 0.55f to 0.08f,
        0.7f to 0.02f, 0.88f to 0.07f, 0.45f to 0.14f
    )

    private fun dibujarEstrellas(lienzo: Canvas) {
        pinturaNube.color = Color.WHITE
        for ((rx, ry) in posicionesEstrellas) {
            lienzo.drawCircle(anchoPantalla * rx, altoPantalla * ry, 3f, pinturaNube)
        }
    }

    private fun dibujarSuelo(lienzo: Canvas, colorCesped: Int, colorTierra: Int) {
        val ySuperficie = altoPantalla * 0.88f

        pinturaSuelo.color = colorTierra
        lienzo.drawRect(0f, ySuperficie, anchoPantalla, altoPantalla, pinturaSuelo)

        pinturaSuelo.color = colorCesped
        lienzo.drawRect(0f, ySuperficie, anchoPantalla, ySuperficie + altoPantalla * 0.03f, pinturaSuelo)
    }
}
