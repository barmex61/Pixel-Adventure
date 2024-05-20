package com.fatih.pixeladventure.screen

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.game.PixelAdventure
import com.fatih.pixeladventure.game.inputMultiplexer
import com.fatih.pixeladventure.parallax.ParallaxBackground
import com.fatih.pixeladventure.ui.model.MenuModel
import com.fatih.pixeladventure.ui.view.levelView
import com.fatih.pixeladventure.ui.view.menuView
import com.fatih.pixeladventure.util.GamePreferences
import com.fatih.pixeladventure.util.MusicAsset
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.graphics.use
import ktx.math.vec2
import ktx.scene2d.actors

class MenuScreen(spriteBatch: SpriteBatch, private val audioService: AudioService,game : PixelAdventure,preferences: GamePreferences) : KtxScreen{


    private val viewPort = StretchViewport(480f,270f)
    private val menuStage = Stage(viewPort,spriteBatch)
    private val menuModel = MenuModel(game,preferences)
    private val parallaxBgd = ParallaxBackground(viewPort,"graphics/gray.png", vec2(1f,1f),1f)

    override fun show() {
        inputMultiplexer.addProcessor(menuStage)
        menuStage.actors {
            menuView()
        }
        audioService.play(MusicAsset.MUSIC2)
    }

    override fun hide() {
        inputMultiplexer.removeProcessor(menuStage)
        menuStage.clear()
    }

    override fun render(delta: Float) {
        viewPort.apply()
        parallaxBgd.scrollBy(-0.5f * delta,-0.5f * delta)
        menuStage.batch.use(viewPort.camera){
            parallaxBgd.draw(0f,0f,it)
        }
        menuStage.act(delta)
        menuStage.draw()

    }

    override fun resize(width: Int, height: Int) {
        menuStage.viewport.update(width,height)
    }

    override fun dispose() {
        inputMultiplexer.clear()
        menuStage.disposeSafely()
        parallaxBgd.disposeSafely()
    }
}
