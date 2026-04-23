package dev.wdona.flappybird.model

import dev.wdona.flappybird.interfaces.Dibujable

class Escenario : Dibujable {
    var darkMode = false
    var colores = listOf<String>()

    override fun actualizarPosicion() {
        TODO("Not yet implemented")
    }

    override fun redibujar() {
        if (darkMode) {
            darkModeColores()
        } else {
            lightModeColores()
        }


    }

    override fun desaparecer() {
        TODO("Not yet implemented")
    }

    fun cambiarModo() {
        darkMode = !darkMode
    }

    fun lightModeColores() {
        // FIXME
        colores = listOf<String>("rojo", "verde", "azul")
    }

    fun darkModeColores() {
        colores = listOf<String>("negro", "gris", "morado")
    }
}