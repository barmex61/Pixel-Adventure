package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import com.fatih.pixeladventure.parallax.ParallaxBackground
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject
import ktx.assets.disposeSafely
import ktx.graphics.use
import ktx.math.vec2

class ParallaxBgdSystem(
    private val gameCamera: OrthographicCamera = inject(),
    private val batch : SpriteBatch = inject(),
    private val gameViewPort: Viewport = inject("gameViewport"),
) : IntervalSystem()  {

    val parallaxBgd = ParallaxBackground(gameViewPort, "graphics/green.png", vec2(0.3f, 0.3f))

    override fun onTick() {
        parallaxBgd.scrollTo(gameCamera.position.x, -gameCamera.position.y)

        gameViewPort.apply()
        batch.use(gameCamera) {
            val paraX = gameCamera.position.x - gameCamera.viewportWidth * 0.5f
            val paraY = gameCamera.position.y - gameCamera.viewportHeight * 0.5f
            parallaxBgd.draw(paraX, paraY, it)
        }
    }

    override fun onDispose() {
        parallaxBgd.disposeSafely()
    }
}
