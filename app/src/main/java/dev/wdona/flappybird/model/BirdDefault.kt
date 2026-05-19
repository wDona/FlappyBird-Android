package dev.wdona.flappybird.model

import android.graphics.*
import dev.wdona.flappybird.interfaces.Dibujable
import dev.wdona.flappybird.interfaces.Soundable

class BirdDefault : Dibujable, Soundable {

    var x = 0f
    var y = 0f
    private var velocidad = 0f
    private val gravedad = 0.7f
    private val fuerzaSalto = -16f
    private val radio = 40f

    private val pinturaCuerpo = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pinturaPico = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pinturaOjo = Paint(Paint.ANTI_ALIAS_FLAG)

    fun inicializar(inicioX: Float, inicioY: Float) {
        x = inicioX
        y = inicioY
        velocidad = 0f
    }

    fun saltar() {
        velocidad = fuerzaSalto
    }

    override fun actualizarPosicion() {
        velocidad += gravedad
        y += velocidad
    }

    override fun redibujar() {}

    override fun desaparecer() {}

    override fun hacerSonido() {}

    fun dibujar(lienzo: Canvas, modoOscuro: Boolean) {
        val colorCuerpo = if (modoOscuro) Color.rgb(255, 140, 0) else Color.rgb(255, 220, 0)
        val rotacion = velocidad.coerceIn(-30f, 30f) * 1.5f

        lienzo.save()
        lienzo.rotate(rotacion, x, y)

        pinturaCuerpo.color = colorCuerpo
        lienzo.drawCircle(x, y, radio, pinturaCuerpo)

        pinturaOjo.color = Color.WHITE
        lienzo.drawCircle(x + radio * 0.4f, y - radio * 0.3f, radio * 0.28f, pinturaOjo)
        pinturaOjo.color = Color.BLACK
        lienzo.drawCircle(x + radio * 0.52f, y - radio * 0.25f, radio * 0.12f, pinturaOjo)

        pinturaPico.color = Color.rgb(255, 100, 0)

        val rutaPico = Path().apply {
            moveTo(x + radio * 0.6f, y - radio * 0.05f)
            lineTo(x + radio * 1.15f, y + radio * 0.08f)
            lineTo(x + radio * 0.6f, y + radio * 0.25f)
            close()
        }

        lienzo.drawPath(rutaPico, pinturaPico)

        lienzo.restore()
    }

    fun obtenerLimites(): RectF {
        val margen = radio * 0.25f
        return RectF(x - radio + margen, y - radio + margen, x + radio - margen, y + radio - margen)
    }
}
