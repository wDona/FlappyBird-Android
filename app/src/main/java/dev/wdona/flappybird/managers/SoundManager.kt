package dev.wdona.flappybird.managers

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.sin

@SuppressLint("StaticFieldLeak")
object SoundManager {
    private var context: Context? = null

    fun inicializar(ctx: Context) {
        context = ctx.applicationContext
    }

    fun liberar() {
        context = null
    }

    fun reproducirSonido(sonido: String) {
        when (sonido) {
            "salto"    -> reproducirTono(880.0, 80, 0.4f)
            "punto"    -> reproducirTono(1320.0, 130, 0.5f)
            "colision" -> reproducirRuido(160, 0.6f)
        }
    }

    private fun reproducirTono(frecuencia: Double, duracionMs: Int, volumen: Float) {
        Thread {
            try {
                val frecuenciaMuestra = 44100
                val cantidadMuestras = frecuenciaMuestra * duracionMs / 1000
                val muestras = ShortArray(cantidadMuestras)

                for (i in 0 until cantidadMuestras) {
                    val angulo = 2.0 * Math.PI * i * frecuencia / frecuenciaMuestra
                    val desvanecimiento = 1f - i.toFloat() / cantidadMuestras
                    muestras[i] = (sin(angulo) * Short.MAX_VALUE * volumen * desvanecimiento).toInt().toShort()
                }

                val pista = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setSampleRate(frecuenciaMuestra)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .build()
                    )
                    .setBufferSizeInBytes(muestras.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                pista.write(muestras, 0, muestras.size)
                pista.play()

                Thread.sleep(duracionMs.toLong() + 60)

                pista.release()
            } catch (_: Exception) {}
        }.start()
    }

    private fun reproducirRuido(duracionMs: Int, volumen: Float) {
        Thread {
            try {
                val frecuenciaMuestra = 44100
                val cantidadMuestras = frecuenciaMuestra * duracionMs / 1000
                val muestras = ShortArray(cantidadMuestras)
                val generador = java.util.Random()

                for (i in 0 until cantidadMuestras) {
                    val desvanecimiento = 1f - i.toFloat() / cantidadMuestras
                    muestras[i] = ((generador.nextFloat() * 2f - 1f) * Short.MAX_VALUE * volumen * desvanecimiento).toInt().toShort()
                }

                val pista = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setSampleRate(frecuenciaMuestra)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .build()
                    )
                    .setBufferSizeInBytes(muestras.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                pista.write(muestras, 0, muestras.size)
                pista.play()
                Thread.sleep(duracionMs.toLong() + 60)
                pista.release()
            } catch (_: Exception) {}
        }.start()
    }
}
