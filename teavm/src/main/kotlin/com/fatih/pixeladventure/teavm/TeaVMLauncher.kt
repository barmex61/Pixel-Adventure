@file:JvmName("TeaVMLauncher")

package com.fatih.pixeladventure.teavm

import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration
import com.github.xpenatan.gdx.backends.teavm.TeaApplication
import com.fatih.pixeladventure.PixelAdventure

/** Launches the TeaVM/HTML application. */
fun main() {
    val config = TeaApplicationConfiguration("canvas").apply {
        width = 640
        height = 480
    }
    TeaApplication(PixelAdventure(), config)
}
