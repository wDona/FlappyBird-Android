package dev.wdona.flappybird.managers

import android.content.Context
import android.widget.VideoView
import androidx.core.net.toUri

object VideoManager {
    fun configurarVideoFinJuego(contexto: Context, videoView: VideoView) {
        try {
            val idRecurso = contexto.resources.getIdentifier("game_over", "raw", contexto.packageName)
            if (idRecurso == 0) return
            val uri = "android.resource://${contexto.packageName}/$idRecurso".toUri()

            videoView.setVideoURI(uri)
            videoView.setOnPreparedListener { reproductor ->
                reproductor.isLooping = true
                videoView.start()
            }
        } catch (_: Exception) {}
    }
}
