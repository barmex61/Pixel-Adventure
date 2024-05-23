package com.fatih.pixeladventure.screen

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.fatih.pixeladventure.ui.view.levelView
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.util.SkinAsset
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actors

class MenuViewTestScreen : KtxScreen {

    private val spriteBatch = SpriteBatch()
    private val viewPort = StretchViewport(320f,180f)
    private val menuStage = Stage(viewPort,spriteBatch)
    private val assets = Assets()


    override fun show() {
        assets.loadAll()
        Scene2DSkin.defaultSkin = assets[SkinAsset.DEFAULT]
        menuStage.actors {
            //levelView()
        }
    }

    override fun render(delta: Float) {
        menuStage.act(delta)
        menuStage.draw()
    }

    override fun dispose() {
        assets.disposeSafely()
        menuStage.disposeSafely()
    }
}
