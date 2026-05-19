package dev.wdona.flappybird.views

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.VideoView
import dev.wdona.flappybird.managers.VideoManager

private class VideoViewPantallaCompleta(context: Context) : VideoView(context) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
    }
}

class GameOverView(
    context: Context,
    private val puntuacion: Int,
    private val alReiniciar: () -> Unit
) : FrameLayout(context) {

    init {
        val videoView = VideoViewPantallaCompleta(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }

        addView(videoView)
        addView(ViewSuperpuesta(context))
        VideoManager.configurarVideoFinJuego(context, videoView)
    }

    inner class ViewSuperpuesta(contexto: Context) : View(contexto) {
        init {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }

        private val fondo = Paint().apply {
            color = Color.argb(185, 10, 10, 50)
        }

        private val titulo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 115f
            color = Color.rgb(255, 70, 70)
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD

            setShadowLayer(10f, 5f, 5f, Color.argb(200, 0, 0, 0))
        }

        private val puntuacion = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 85f
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD

            setShadowLayer(7f, 4f, 4f, Color.argb(160, 0, 0, 0))
        }

        private val boton = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(50, 185, 50)
        }

        private val textoBoton = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 65f
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }

        override fun onDraw(lienzo: Canvas) {
            val anchoPantalla = lienzo.width.toFloat()
            val altoPantalla = lienzo.height.toFloat()

            lienzo.drawRect(0f, 0f, anchoPantalla, altoPantalla, fondo)

            lienzo.drawText("Game Over", anchoPantalla / 2, altoPantalla * 0.30f, titulo)
            lienzo.drawText("Puntuación: ${this@GameOverView.puntuacion}", anchoPantalla / 2, altoPantalla * 0.46f,
                this@ViewSuperpuesta.puntuacion
            )
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            if (event.action == MotionEvent.ACTION_DOWN) alReiniciar()

            return true
        }
    }
}
