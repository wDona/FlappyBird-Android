package dev.wdona.flappybird

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.wdona.flappybird.views.MenuView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(MenuView(this))
    }
}