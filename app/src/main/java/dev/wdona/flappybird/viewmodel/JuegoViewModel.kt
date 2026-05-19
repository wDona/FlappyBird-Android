package dev.wdona.flappybird.viewmodel

import dev.wdona.flappybird.model.BirdDefault
import dev.wdona.flappybird.model.Escenario
import dev.wdona.flappybird.model.HUD
import dev.wdona.flappybird.model.Obstaculo

class JuegoViewModel {

    enum class Estado { ESPERANDO, JUGANDO, FIN }

    var estado = Estado.ESPERANDO
        private set

    val pajaro = BirdDefault()
    val escenario = Escenario()
    val hud = HUD()
    val obstaculos = mutableListOf<Obstaculo>()

    var modoOscuro = false
        private set

    var anchoPantalla = 0f
    var altoPantalla = 0f

    var onPerderListener: ((Int) -> Unit)? = null
    var onPuntuarListener: (() -> Unit)? = null
    var onSaltarListener: (() -> Unit)? = null

    private var fotogramasDesdeUltimoObstaculo = 0
    private val intervaloObstaculos = 90

    fun inicializar(ancho: Float, alto: Float) {
        anchoPantalla = ancho
        altoPantalla = alto

        pajaro.inicializar(ancho * 0.25f, alto * 0.5f)
        escenario.inicializar(ancho, alto)

        obstaculos.clear()

        hud.puntuacion = 0

        fotogramasDesdeUltimoObstaculo = 0

        estado = Estado.ESPERANDO
    }

    fun saltar() {
        if (estado == Estado.ESPERANDO) estado = Estado.JUGANDO

        if (estado == Estado.JUGANDO) {
            pajaro.saltar()
            onSaltarListener?.invoke()
        }
    }

    fun actualizar() {
        if (estado != Estado.JUGANDO) return

        escenario.actualizarPosicion()
        pajaro.actualizarPosicion()

        generarObstaculoSiToca()

        actualizarObstaculos()
    }

    fun cambiarModoOscuro(oscuro: Boolean) {
        modoOscuro = oscuro
        escenario.modoOscuro = oscuro
    }

    private fun generarObstaculoSiToca() {
        fotogramasDesdeUltimoObstaculo++

        if (fotogramasDesdeUltimoObstaculo >= intervaloObstaculos) {
            fotogramasDesdeUltimoObstaculo = 0
            obstaculos.add(Obstaculo(anchoPantalla, altoPantalla))
        }
    }

    private fun actualizarObstaculos() {
        val iterador = obstaculos.iterator()

        while (iterador.hasNext()) {
            val obstaculo = iterador.next()

            obstaculo.actualizarPosicion()

            verificarPuntuacion(obstaculo)

            if (obstaculo.isFueraDePantalla()) {
                iterador.remove()
                continue
            }

            if (isColisionConObstaculo(obstaculo)) return
        }

        verificarColisionBordes()
    }

    private fun verificarPuntuacion(obstaculo: Obstaculo) {
        if (!obstaculo.superado && obstaculo.x + obstaculo.ancho < pajaro.x) {
            obstaculo.superado = true
            hud.puntuacion++
            onPuntuarListener?.invoke()
        }
    }

    private fun isColisionConObstaculo(obstaculo: Obstaculo): Boolean {
        if (obstaculo.colisionCon(pajaro.obtenerLimites())) {
            terminarJuego()
            return true
        }

        return false
    }

    private fun verificarColisionBordes() {
        val ySuperficie = altoPantalla * 0.88f

        if (pajaro.y - 40f > ySuperficie || pajaro.y + 40f < 0f) {
            terminarJuego()
        }
    }

    private fun terminarJuego() {
        estado = Estado.FIN
        onPerderListener?.invoke(hud.puntuacion)
    }
}
