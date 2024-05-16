package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.event.GameResizeEvent
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
) : IntervalSystem() , GameEventListener {

    private val parallaxBgd = ParallaxBackground(gameViewPort, vec2(0.33f,0.05f))

    override fun onTick() {
        parallaxBgd.scrollTo(gameCamera.position.x ,-gameCamera.position.y )
        gameViewPort.apply()
        batch.use (gameCamera){
            parallaxBgd.draw(
                gameCamera.position.x - gameCamera.viewportWidth * 0.5f,
                gameCamera.position.y - gameCamera.viewportHeight * 0.5f,
                it)
        }
    }

    override fun onEvent(gameEvent: GameEvent) {
        when(gameEvent){
            is GameResizeEvent ->{
            }
            else -> Unit

        }
    }

    override fun onDisable() {
        parallaxBgd.disposeSafely()
    }
}
