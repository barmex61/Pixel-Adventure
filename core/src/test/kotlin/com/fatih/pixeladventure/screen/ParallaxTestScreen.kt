package com.fatih.pixeladventure.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.fatih.pixeladventure.parallax.ParallaxBackground
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.graphics.use
import ktx.math.vec2

class ParallaxTestScreen : KtxScreen {

    private val spriteBatch = SpriteBatch()
    private val gameViewPort = StretchViewport(16f,9f)
    private val parallaxBgd = ParallaxBackground(gameViewPort,"graphics/green.png", vec2(0.33f,0.05f))
    private val gameCamera = gameViewPort.camera


    override fun render(delta: Float) {
        parallaxBgd.scrollTo(gameCamera.position.x ,-gameCamera.position.y )
        gameViewPort.apply()
        spriteBatch.use (gameCamera){
            parallaxBgd.draw(
                gameCamera.position.x - gameCamera.viewportWidth * 0.5f,
                gameCamera.position.y - gameCamera.viewportHeight * 0.5f,
                it)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)){
            gameCamera.position.x -= 0.5f
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.D)){
            gameCamera.position.x += 0.5f
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.W)){
            gameCamera.position.y += 0.5f
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.S)){
            gameCamera.position.y -= 0.5f
        }
    }

    override fun resize(width: Int, height: Int) {
        gameViewPort.update(width,height,true)
    }

    override fun dispose() {
        parallaxBgd.disposeSafely()
        spriteBatch.disposeSafely()
    }
}
