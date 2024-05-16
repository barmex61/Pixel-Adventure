package com.fatih.pixeladventure.screen

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.fatih.pixeladventure.parallax.ParallaxBackground
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class ParallaxTestScreen : KtxScreen {

    private val viewPort = StretchViewport(16f,9f)
    private val parallaxBackground = ParallaxBackground(viewPort)
    private val spriteBatch = SpriteBatch()


    override fun render(delta: Float) {
        viewPort.apply()
        spriteBatch.use(viewPort.camera) {
            parallaxBackground.scrollBy(0.01f,0.01f)
            parallaxBackground.draw(0f,0f,it)
        }
    }

    override fun resize(width: Int, height: Int) {
        viewPort.update(width,height,true)
        parallaxBackground.resize(width.toFloat(),height.toFloat())
    }

    override fun dispose() {
        parallaxBackground.disposeSafely()
        spriteBatch.disposeSafely()
    }
}
