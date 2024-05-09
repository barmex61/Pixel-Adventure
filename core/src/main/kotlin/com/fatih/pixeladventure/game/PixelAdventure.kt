package com.fatih.pixeladventure.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World
import com.fatih.pixeladventure.screen.LoadingScreen
import com.fatih.pixeladventure.util.Assets
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.disposeSafely

typealias PhysicWorld = World

class PixelAdventure : KtxGame<KtxScreen>() {

    private val spriteBatch : SpriteBatch by lazy { SpriteBatch() }
    private val assets : Assets by lazy { Assets() }

    override fun create() {
        addScreen(LoadingScreen(spriteBatch, assets))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        spriteBatch.disposeSafely()
        assets.dispose()
    }

    companion object{
        const val UNIT_SCALE = 1/16f
    }
}
